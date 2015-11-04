package com.example.okylifeapp.app.activities;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.*;
import android.widget.*;
import aplication.OkyLife;
import com.example.okylifeapp.app.R;
import dialogs.LogoutDialog;
import rest.AsyncResponse;

/**
 * Created by Cristian Parada on 18/10/2015.
 */
public class DoingSportActivity extends Activity implements AsyncResponse, LogoutDialog.AlertPositiveLogoutListener {
    Button btnPlay;
    Button btnStop;
    Button btnSave;
    Chronometer chronometer;
    Spinner sport;
    EditText distanceObjective;
    String currenTime = "";
    //Valores a Guardar//
    String calories = "0 cal", duration = "00:00", distance = "0 Km",
            velocity = "0 km/h", rate = "0 min/Km", objective = "0 Km", hydration = "0.00 Lt";

    long elapsedTime = 0;
    boolean isPlaying = false, init = true;
    String[] strings = {"Correr", "Ciclismo", "Caminar"};

    String[] subs = {"\"Hay gente que corre mas rapido que tu, pero no lo disfruta tanto\"",
            "\"No se deja de pedalear cuando se envejece. Se envejece cuando se deja de pedalear\"",
            "\"Cuando salgas a caminar, hazlo despacio, descubriras tantas cosas hermosas.\""};

    int arr_images[] = {R.drawable.run,
            R.drawable.cycling,
            R.drawable.walk};


    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.doing_sports_activity);
        sport = (Spinner) findViewById(R.id.spinner);
        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnSave = (Button) findViewById(R.id.btnSaveActivity);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        distanceObjective = (EditText) findViewById(R.id.inputObjective);
        btnStop.setEnabled(false);
        btnSave.setEnabled(false);

        sport.setAdapter(new MyAdapter(this, R.layout.row, strings));
        btnPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sport.setEnabled(false);
                btnStop.setEnabled(true);
                btnSave.setEnabled(false);
                distanceObjective.setEnabled(false);
                isPlaying = !isPlaying;
                if (isPlaying) {
                    if (init == true) {
                        chronometer.setBase(SystemClock.elapsedRealtime());
                    }
                    chronometer.start();
                    pause();
                } else {
                    init = false;
                    chronometer.stop();
                    chronometer.setText(currenTime);
                    btnSave.setEnabled(true);
                    play();
                }
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                chronometer.stop();
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.setText("00:00");
                isPlaying = false;
                init = true;
                btnPlay.setEnabled(true);
                play();
                btnStop.setEnabled(false);
                btnSave.setEnabled(false);
                sport.setEnabled(true);
                distanceObjective.setEnabled(true);
            }
        });

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {

                if (isPlaying == true) {
                    if (init == true) {
                        long minutes = ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000) / 60;
                        String min = "" + minutes;
                        long seconds = ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000) % 60;
                        String seg = "" + seconds;
                        if (minutes < 10) {
                            min = "0" + min;
                        }
                        if (seconds < 10) {
                            seg = "0" + seg;
                        }
                        currenTime = min + ":" + seg;
                        chronometer.setText(currenTime);
                        elapsedTime = SystemClock.elapsedRealtime();
                    } else {
                        long minutes = ((elapsedTime - chronometer.getBase()) / 1000) / 60;
                        String min = "" + minutes;
                        long seconds = ((elapsedTime - chronometer.getBase()) / 1000) % 60;
                        String seg = "" + seconds;
                        if (minutes < 10) {
                            min = "0" + min;
                        }
                        if (seconds < 10) {
                            seg = "0" + seg;
                        }

                        currenTime = min + ":" + seg;
                        chronometer.setText(currenTime);
                        elapsedTime = elapsedTime + 1000;
                    }


                } else if (isPlaying == false && init == false) {
                    long minutes = ((elapsedTime - chronometer.getBase()) / 1000) / 60;
                    String min = "" + minutes;
                    long seconds = ((elapsedTime - chronometer.getBase()) / 1000) % 60;
                    String seg = "" + seconds;
                    if (minutes < 10) {
                        min = "0" + min;
                    }
                    if (seconds < 10) {
                        seg = "0" + seg;
                    }
                    currenTime = min + ":" + seg;
                    chronometer.setText(currenTime);
                    elapsedTime = elapsedTime + 1000;
                }
            }
        });
    }

    public void renderSaveSportActivityView(View view) {
        objective = distanceObjective.getText().toString() + " Km";
        duration = chronometer.getText().toString();
        Intent saveActivityIntent = new Intent(this, SaveSportActivity.class);
        Bundle values = new Bundle();
        values.putString("calories", calories);
        values.putString("duration", duration);
        values.putString("distance", distance);
        values.putString("velocity", velocity);
        values.putString("rate", rate);
        values.putString("objective", objective);
        values.putString("hydration", hydration);
        saveActivityIntent.putExtras(values);
        finish();
        startActivity(saveActivityIntent);

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void processFinish(String result) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_oky_life, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                showLogoutDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showLogoutDialog() {
        FragmentManager manager = getFragmentManager();
        LogoutDialog logoutDialog = new LogoutDialog();
        logoutDialog.show(manager, "Logout");
    }

    @Override
    public void onPositiveLogoutClick(boolean logout) {
        OkyLife.deleteAccount(AccountManager.get(getApplicationContext()), ((OkyLife) getApplication()).getOkyLifeAccount());
        Intent intent = new Intent(this, OkyLifeStartActivity.class);
        startActivity(intent);
        finish();
    }

    public void pause() {
        btnPlay.setBackgroundResource(R.drawable.pause);
    }

    public void play() {
        btnPlay.setBackgroundResource(R.drawable.play);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public class MyAdapter extends ArrayAdapter<String> {

        public MyAdapter(Context context, int textViewResourceId, String[] objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.row, parent, false);
            TextView label = (TextView) row.findViewById(R.id.company);
            label.setText(strings[position]);

            TextView sub = (TextView) row.findViewById(R.id.sub);
            sub.setText(subs[position]);

            ImageView icon = (ImageView) row.findViewById(R.id.image);
            icon.setImageResource(arr_images[position]);

            return row;
        }
    }

}
