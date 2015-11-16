package com.example.okylifeapp.app.activities;

import android.accounts.Account;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import aplication.OkyLife;
import com.example.okylifeapp.app.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.plus.Plus;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import rest.AsyncResponse;

import java.util.ArrayList;


/**
 * Created by Cristian Parada on 18/10/2015.
 */
public class SaveSportActivity extends FragmentActivity implements AsyncResponse, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
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
    JSONArray locations;
    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;
    private Location mLastLocation;
    private Account okyLifeAccount;


    private GoogleMap mMap;


    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.save_sport_activity);
        okyLifeAccount = ((OkyLife) getApplication()).getOkyLifeAccount();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Plus.API)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        setFileds();
        setMap();
    }

    public void setMap() {
        FragmentManager fmanager = getSupportFragmentManager();
        mMap = ((SupportMapFragment) fmanager.findFragmentById(R.id.save_sport_map)).getMap();
        mMap.setMyLocationEnabled(true);
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
        try {
            locations = new JSONArray(getIntent().getExtras().getString("locations"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int hours = (int) (duration / 3600);
        int minutes = (int) ((duration % 3600) / 60);
        int seconds = (int) (duration % 60);
        String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        caloriesText.setText(String.format("%.2f", calories));
        distanceText.setText(String.format("%.2f", distance));
        velocityText.setText(String.format("%.2f", velocity));
        rhythmText.setText(String.format("%.2f", rhythm));
        targetDistanceText.setText(String.format("%.2f", targetDistance));
        hydrationText.setText(String.format("%.2f", hydration));
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

        if (mLastLocation != null) {
            params.add(new BasicNameValuePair("latitude", String.valueOf(mLastLocation.getLatitude())));
            params.add(new BasicNameValuePair("longitude", String.valueOf(mLastLocation.getLongitude())));
        }

        ((OkyLife) getApplication()).getMasterCaller().postData("SportActivity/createSportActivity", this, params);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void processFinish(String result) {
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart() {
        Log.v("location", "started");
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        Log.v("location", "stopped");
        super.onStop();
        mGoogleApiClient.disconnect();
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.v("location", "connected");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v("google", "failed" + connectionResult);
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (connectionResult.hasResolution()) {
            try {
                mResolvingError = true;
                connectionResult.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            Log.e("google", String.valueOf(connectionResult.getErrorCode()));
            mResolvingError = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            mResolvingError = false;
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        }
    }
}
