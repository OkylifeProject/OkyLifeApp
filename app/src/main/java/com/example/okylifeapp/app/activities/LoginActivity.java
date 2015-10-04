package com.example.okylifeapp.app.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import aplication.OkyLife;
import com.example.okylifeapp.app.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import rest.AsyncResponse;

/**
 * Created by Cristian Parada on 03/10/2015.
 */
public class LoginActivity extends Activity implements AsyncResponse {
    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.login);
        info = (TextView)findViewById(R.id.info);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                info.setText(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken()
                );
            }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                info.setText("Login attempt failed.");
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void processFinish(String response) {
        Toast.makeText(this, response, Toast.LENGTH_LONG).show();
        Log.v("response", response);
    }

    public void login(View view) {
        showDialog(0);
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Create the quit confirmation dialog

        builder.setMessage("en Construccion...");
        dialog = builder.create();

        return dialog;
    }


}
