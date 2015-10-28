package com.example.okylifeapp.app.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import com.example.okylifeapp.app.R;
import rest.AsyncResponse;


/**
 * Created by Cristian Parada on 18/10/2015.
 */
public class SaveSportActivity extends Activity implements AsyncResponse {
    TextView calories,duration,distance,velocity,rate,objective,hydration;
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.save_sport_activity);
        Bundle values = getIntent().getExtras();
        calories = (TextView)findViewById(R.id.textCalories);
        duration = (TextView)findViewById(R.id.textDuration);
        distance = (TextView)findViewById(R.id.textDistance);
        velocity = (TextView)findViewById(R.id.textVelocity);
        rate = (TextView)findViewById(R.id.textRate);
        objective = (TextView)findViewById(R.id.textObjective);
        hydration = (TextView)findViewById(R.id.textHydration);

        calories.setText(values.getString("calories"));
        duration.setText(values.getString("duration"));
        distance.setText(values.getString("rate"));
        velocity.setText(values.getString("distance"));
        rate.setText(values.getString("velocity"));
        objective.setText(values.getString("objective"));
        hydration.setText(values.getString("hydration"));

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
    @Override
    public void onStart() {
        super.onStart();



    }


    @Override
    public void processFinish(String result) {

    }




}
