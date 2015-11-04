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

    private Account okyLifeAccount;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.save_sport_activity);
        okyLifeAccount = ((OkyLife) getApplication()).getOkyLifeAccount();
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
        String type = "Walk";

        TextView calories = (TextView) findViewById(R.id.textCalories);
        TextView distance = (TextView) findViewById(R.id.textDistance);
        TextView velocity = (TextView) findViewById(R.id.textVelocity);
        TextView rhythm = (TextView) findViewById(R.id.textRhythm);
        TextView targetDistance = (TextView) findViewById(R.id.textObjective);
        TextView hydration = (TextView) findViewById(R.id.textHydration);

        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", okyLifeAccount.name));
        params.add(new BasicNameValuePair("name", title.getText().toString()));
        params.add(new BasicNameValuePair("type", type));
        params.add(new BasicNameValuePair("description", description.getText().toString()));
        params.add(new BasicNameValuePair("calories", calories.getText().toString()));
        params.add(new BasicNameValuePair("distance", distance.getText().toString()));
        params.add(new BasicNameValuePair("velocity", velocity.getText().toString()));
        params.add(new BasicNameValuePair("rhythm", rhythm.getText().toString()));
        params.add(new BasicNameValuePair("targetDistance", targetDistance.getText().toString()));
        params.add(new BasicNameValuePair("hydration", hydration.getText().toString()));

        ((OkyLife) getApplication()).getMasterCaller().postData("SportActivity/createSportActivity", this, params);

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
