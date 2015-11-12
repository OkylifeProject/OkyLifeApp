package com.example.okylifeapp.app.activities;

import android.accounts.Account;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import aplication.OkyLife;
import com.example.okylifeapp.app.R;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import rest.AsyncResponse;

import java.util.ArrayList;


/**
 * Created by Cristian Parada on 18/10/2015.
 */
public class SaveSportActivity extends Activity implements AsyncResponse {

    /**
     * ACTIVITY FIELDS
     **/
    double calories;
    double distance;
    double velocity;
    double rhythm;
    double targetDistance;
    double hydration;
    double duration;
    String type;
    private Account okyLifeAccount;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.save_sport_activity);
        okyLifeAccount = ((OkyLife) getApplication()).getOkyLifeAccount();

        setFileds();
    }

    public void setFileds() {
        TextView caloriesText = (TextView) findViewById(R.id.textCalories);
        TextView distanceText = (TextView) findViewById(R.id.textDistance);
        TextView velocityText = (TextView) findViewById(R.id.textVelocity);
        TextView rhythmText = (TextView) findViewById(R.id.textRhythm);
        TextView targetDistanceText = (TextView) findViewById(R.id.textObjective);
        TextView hydrationText = (TextView) findViewById(R.id.textHydration);
        TextView durationText = (TextView) findViewById(R.id.textDuration);

        type = getIntent().getExtras().getString("type");
        calories = getIntent().getExtras().getDouble("calories");
        distance = getIntent().getExtras().getDouble("distance");
        velocity = getIntent().getExtras().getDouble("velocity");
        rhythm = getIntent().getExtras().getDouble("rhythm");
        targetDistance = getIntent().getExtras().getDouble("targetDistance");
        hydration = getIntent().getExtras().getDouble("hydration");
        duration = getIntent().getExtras().getDouble("duration");

        int hours = (int) (duration / 3600);
        int minutes = (int) ((duration % 3600) / 60);
        int seconds = (int) (duration % 60);
        String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        caloriesText.setText(String.valueOf(calories));
        distanceText.setText(String.valueOf(distance));
        velocityText.setText(String.valueOf(velocity));
        rhythmText.setText(String.valueOf(rhythm));
        targetDistanceText.setText(String.valueOf(targetDistance));
        hydrationText.setText(String.valueOf(hydration));
        durationText.setText(String.valueOf(timeString));

    }

    public void renderDialogCancelConfirmation(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Atencion")
                .setMessage("Esta a punto de borrar el registro de la actividad realizada. \nDesea Continuar?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        renderStartActivity();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void renderStartActivity() {
        Intent startActivityIntent = new Intent(this, StartActivity.class);
        startActivity(startActivityIntent);
        finish();
    }

    public void saveSportActivity(View view) {
        EditText title = (EditText) findViewById(R.id.sport_title);
        EditText description = (EditText) findViewById(R.id.sport_description);

        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", okyLifeAccount.name));
        params.add(new BasicNameValuePair("name", title.getText().toString()));
        params.add(new BasicNameValuePair("type", String.valueOf(type)));

        params.add(new BasicNameValuePair("description", description.getText().toString()));
        params.add(new BasicNameValuePair("calories", String.valueOf(calories)));
        params.add(new BasicNameValuePair("distance", String.valueOf(distance)));
        params.add(new BasicNameValuePair("velocity", String.valueOf(velocity)));
        params.add(new BasicNameValuePair("rhythm", String.valueOf(rhythm)));
        params.add(new BasicNameValuePair("targetDistance", String.valueOf(targetDistance)));
        params.add(new BasicNameValuePair("duration", String.valueOf(duration)));
        params.add(new BasicNameValuePair("hydration", String.valueOf(hydration)));

        ((OkyLife) getApplication()).getMasterCaller().postData("SportActivity/createSportActivity", this, params);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void processFinish(String result) {
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
    }


}
