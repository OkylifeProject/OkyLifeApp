package com.example.okylifeapp.app.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.example.okylifeapp.app.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.plus.Plus;
import dialogs.LogoutDialog;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import rest.AsyncResponse;

import java.util.ArrayList;


/**
 * Created by Cristian Parada on 18/10/2015.
 */
public class EatActivity extends Activity implements AsyncResponse, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    static final int ADD_ALIMENT_REQUEST = 1;
    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    JSONArray jsonAliments;

    String[] nameAliments;
    String[] descriptionAliments;
    /**
     * SPINNER OPTIONS
     **/
    String[] strings = {"Breakfast", "Lunch", "Dinner", "Snack", "Aperitif"};
    String[] subs = {"\"Empieza el dia con un buen desayudo\"",
            "\"Almuerza como principe\"",
            "\"Date gusto con una rica cena\"",
            "\"No te quedes con las ganas\"",
            "\"Que sirva como abrebocas\""
    };
    int arr_images[] = {R.drawable.breakfast,
            R.drawable.lunch,
            R.drawable.dinner,
            R.drawable.snack,
            R.drawable.aperitif
    };
    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;
    private Location mLastLocation;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.eat_activity);
        jsonAliments = new JSONArray();
        setFields();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Plus.API)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public void setFields() {
        Spinner FoodSpinner = (Spinner) findViewById(R.id.food_spinner);
        FoodSpinner.setAdapter(new MyAdapter(getApplicationContext(), R.layout.row, strings));
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
        /*
        Log.v("location", "connected");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        Log.v("locationLatitude", String.valueOf(mLastLocation.getLatitude()));
        Log.v("locationLongitude", String.valueOf(mLastLocation.getLongitude()));
        */
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


    public void renderAddAlimentView(View view) {
        Intent addAliment = new Intent(getApplicationContext(), AddAlimentActivity.class);
        startActivityForResult(addAliment, ADD_ALIMENT_REQUEST);
    }

    public void saveEatActivity(View view) throws JSONException {
        String jsonQuery = "{ingredients:" + jsonAliments.toString() + "}";
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("ingredients", jsonQuery));

        //TODO add the default EAT acitivity fields

        if (mLastLocation != null) {
            params.add(new BasicNameValuePair("latitude", String.valueOf(mLastLocation.getLatitude())));
            params.add(new BasicNameValuePair("longitude", String.valueOf(mLastLocation.getLongitude())));
        }
        Log.v("ingredients", params.toString());
    }

    /*
    public void renderAddFoodView(View view) {

        Intent saveActivityIntent = new Intent(this, AddFoodActivity.class);
        finish();
        startActivity(saveActivityIntent);
    }
    */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_ALIMENT_REQUEST) {
            if (resultCode == RESULT_OK) {
                try {
                    JSONObject jsonAliment = new JSONObject(data.getStringExtra("jsonAliment"));
                    jsonAliments.put(jsonAliment);
                    showAliments();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_LONG).show();
            }
        }
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

    public void showAliments() {
        ListView alimentList = (ListView) findViewById(R.id.listAliments);
        nameAliments = new String[jsonAliments.length()];
        descriptionAliments = new String[jsonAliments.length()];

        for (int i = 0; i < jsonAliments.length(); i++) {
            try {
                JSONObject aliment = (JSONObject) jsonAliments.get(i);
                nameAliments[i] = aliment.getString("name");
                descriptionAliments[i] = aliment.getString("description");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        alimentList.setAdapter(new MyAlimentsAdapter(this, R.layout.row, nameAliments));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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

    public class MyAlimentsAdapter extends ArrayAdapter<String> {

        public MyAlimentsAdapter(Context context, int textViewResourceId, String[] objects) {
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
            View row = inflater.inflate(R.layout.aliment_row, parent, false);
            TextView nameText = (TextView) row.findViewById(R.id.aliment_name);
            TextView descriptionText = (TextView) row.findViewById(R.id.aliment_description);
            nameText.setText(nameAliments[position]);
            descriptionText.setText(descriptionAliments[position]);
            return row;
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
