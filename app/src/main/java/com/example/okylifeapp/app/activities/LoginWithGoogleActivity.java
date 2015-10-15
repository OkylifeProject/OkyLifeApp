package com.example.okylifeapp.app.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import aplication.OkyLife;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import rest.AsyncResponse;

import java.util.ArrayList;

/**
 * Created by mordreth on 10/4/15.
 */
public class LoginWithGoogleActivity extends Activity implements AsyncResponse, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;
    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    AccountManager accountManager;
    Account okyLifeAccount;
    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;
    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;
    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;

    @Override
    public void onCreate(Bundle savedInstance) {
        Log.v("google", "created");
        super.onCreate(savedInstance);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        /**ACCOUNT MANAGER **/
        accountManager = AccountManager.get(getApplicationContext());
    }

    @Override
    public void onStart() {
        Log.v("google", "started");
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        Log.v("google", "stopped");
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.v("google", "connected");
        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            String password = ((OkyLife) getApplication()).SHA1(currentPerson.getId());

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("email", String.valueOf(Plus.AccountApi.getAccountName(mGoogleApiClient))));
            params.add(new BasicNameValuePair("password", String.valueOf(password)));

            ((OkyLife) getApplication()).getMasterCaller().postData("User/loginUser", this, params);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v("google", "suspended");

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.v("google", "failed" + result);
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            Log.e("google", String.valueOf(result.getErrorCode()));
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

    @Override
    public void processFinish(String response) {
        Log.v("response", "proccess finished");
        JSONObject jsonObject = new JSONObject();
        if (((OkyLife) getApplication()).isJSON(response)) {
            try {
                Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
                jsonObject = new JSONObject(response);
                OkyLife.createAccountFirstTime(accountManager, jsonObject);
                Log.v("response", "success register");
                Intent intent = new Intent(this, OkyLifeStartActivity.class);
                finish();
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, response, Toast.LENGTH_LONG).show();
            Log.v("response", "error");
            finish();
        }
    }

}
