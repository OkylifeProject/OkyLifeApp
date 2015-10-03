package com.example.okylifeapp.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import aplication.OkyLife;
import com.example.okylifeapp.app.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import rest.AsyncResponse;

/**
 * Created by mordreth on 10/2/15.
 */
public class RegisterActivity extends Activity implements AsyncResponse, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {
    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;
    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PLUS_ME))
                .addScope(new Scope(Scopes.PROFILE))
                .build();

    }

    @Override
    public void processFinish(String response) {
        Toast.makeText(this, response, Toast.LENGTH_LONG).show();
        Log.v("response", response);
    }

    public void register(View view) {
        /*Prueba para POST*/
        /*
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("registerType", "Api"));
        params.add(new BasicNameValuePair("sex", "Male"));
        params.add(new BasicNameValuePair("firstName", "Alejo"));
        params.add(new BasicNameValuePair("lastName", "Velasco"));
        params.add(new BasicNameValuePair("password", "Lanzarote222"));
        params.add(new BasicNameValuePair("email", "savelascod@gmail.com"));
        params.add(new BasicNameValuePair("birthDate", "5/3/1994"));

        ((OkyLife) getApplication()).getMasterCaller().postData("User/registerUser", this, params);
        */
        /*Prueba para GET*/
        ((OkyLife) getApplication()).getMasterCaller().getById("User", 1, this);
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
