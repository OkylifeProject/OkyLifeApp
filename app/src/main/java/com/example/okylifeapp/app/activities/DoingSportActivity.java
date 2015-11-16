package com.example.okylifeapp.app.activities;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.*;
import android.widget.*;
import aplication.OkyLife;
import com.example.okylifeapp.app.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import data.User;
import dialogs.LogoutDialog;
import rest.AsyncResponse;

import java.util.ArrayList;

/**
 * Created by Cristian Parada on 18/10/2015.
 */
public class DoingSportActivity extends Activity implements AsyncResponse, LogoutDialog.AlertPositiveLogoutListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    Button btnPlay;
    Button btnStop;
    Button btnSave;
    Chronometer chronometer;
    Spinner sport;
    EditText distanceObjective;
    TextView velocityText;
    TextView traveledDistanceText;
    String currenTime = "";

    User user;

    //Valores a Guardar//
    double calories = 0;
    double duration = 0;
    double distance = 0;
    double velocity = 0;
    double rate = 0;
    double targetDistance = 0;
    double hydration = 0;
    ArrayList<data.Location> locations;
    String[] effortType = {"Low", "High", "Medium",};

    long elapsedTime = 0;
    boolean isPlaying = false, init = true;
    String[] strings = {"Run", "Biking", "Walk"};

    String[] subs = {"\"Hay gente que corre mas rapido que tu, pero no lo disfruta tanto\"",
            "\"No se deja de pedalear cuando se envejece. Se envejece cuando se deja de pedalear\"",
            "\"Cuando salgas a caminar, hazlo despacio, descubriras tantas cosas hermosas.\""};

    int arr_images[] = {R.drawable.run,
            R.drawable.cycling,
            R.drawable.walk};
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;


    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.doing_sports_activity);
        user = ((OkyLife) getApplication()).getUser();
        setFields();
        createLocationRequest();
        buildGoogleApiClient();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void setFields() {
        locations = new ArrayList<data.Location>();
        sport = (Spinner) findViewById(R.id.sportSpinner);
        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnSave = (Button) findViewById(R.id.btnSaveActivity);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        distanceObjective = (EditText) findViewById(R.id.inputObjective);
        velocityText = (TextView) findViewById(R.id.textVelocitySport);
        traveledDistanceText = (TextView) findViewById(R.id.textDistanceSport);
        btnStop.setEnabled(false);
        btnSave.setEnabled(false);
    }

    public void setChronometer() {
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
                    startLocationUpdates();
                    pause();
                } else {
                    init = false;
                    chronometer.stop();
                    chronometer.setText(currenTime);
                    btnSave.setEnabled(true);
                    stopLocationUpdates();
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
                stopLocationUpdates();
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
        EditText targetDistanceText = (EditText) findViewById(R.id.inputObjective);
        Spinner sportTypeSpinner = (Spinner) findViewById(R.id.sportSpinner);
        duration = (SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000;
        targetDistance = Double.valueOf(targetDistanceText.getText().toString());

        Intent saveActivityIntent = new Intent(this, SaveSportActivity.class);
        Bundle values = new Bundle();
        values.putDouble("calories", calories);
        values.putDouble("duration", duration);
        values.putDouble("distance", distance);
        values.putDouble("velocity", velocity);
        values.putDouble("rhythm", rate);
        values.putDouble("targetDistance", targetDistance);
        values.putString("type", strings[sportTypeSpinner.getSelectedItemPosition()]);
        saveActivityIntent.putExtras(values);

        startActivity(saveActivityIntent);
        finish();

    }

    @Override
    public void onStart() {
        Log.v("locationUp", "failed");
        super.onStart();
        mGoogleApiClient.connect();

    }

    @Override
    public void onStop() {
        Log.v("locationUp", "failed");
        super.onStop();
        stopLocationUpdates();
        mGoogleApiClient.disconnect();
    }

    protected void stopLocationUpdates() {
        Log.v("locationUp", "stopped");
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
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
    protected void onPause() {
        super.onPause();
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

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v("locationUp", "failed");
        mGoogleApiClient.connect();
    }

    protected void createLocationRequest() {
        Log.v("locationUp", "service-created");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.v("locationUp", "connected");
        //startLocationUpdates();
        setChronometer();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v("locationUp", "suspended");

    }

    protected void startLocationUpdates() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Log.v("locationUp", "service-started");
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v("locationUp", "changed");

        data.Location obtainedLocation = new data.Location(location,
                ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000));
        locations.add(obtainedLocation);
        updateActivityFields();
    }

    public void updateActivityFields() {
        if (locations.size() > 1) {
            switch (sport.getSelectedItemPosition()) {
                case 0:

            }
            data.Location locationA = locations.get(locations.size() - 2);
            data.Location locationB = locations.get(locations.size() - 1);
            data.Location firstLocation = locations.get(0);
            double localDistance = locationA.getLocation().distanceTo(locationB.getLocation());
            double localTime = locationB.getTime() - locationA.getTime();
            distance = firstLocation.getLocation().distanceTo(locationB.getLocation());
            velocity = localDistance / localTime;
            traveledDistanceText.setText(String.format("%.2f", localDistance) + "m");
            velocityText.setText(String.format("%.2f", velocity) + " m/s");
            calories = OkyLife.calculateCaloriesByDistance("", distance, Double.valueOf(user.getWeight()), velocity);
            rate = velocity / distance;
        }
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
