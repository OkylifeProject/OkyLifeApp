package com.example.okylifeapp.app.activities;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import rest.AsyncResponse;

import java.util.ArrayList;

/**
 * Created by Cristian Parada on 03/10/2015.
 */
public class LoginActivity extends Activity implements AsyncResponse {
    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    AccountManager accountManager;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        /**FACEBOOK**/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.login);
        info = (TextView) findViewById(R.id.info);
        loginButton = (LoginButton) findViewById(R.id.login_button);
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

        /**GOOGLE**/

        findViewById(R.id.sign_in_button_google).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithGoogle();
            }
        });

        /**ACCOUNT MANAGER **/
        accountManager = AccountManager.get(getApplicationContext());

    }


    public void loginWithGoogle() {
        Intent intent = new Intent(this, LoginWithGoogleActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void processFinish(String response) {
        Log.v("response", response);
        if (OkyLife.isJSON(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                OkyLife.createAccountFirstTime(accountManager, jsonObject);
                Log.v("response", "success register");
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG);
        }
    }


    public void login(View view) {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        TextView emailText = (TextView) findViewById(R.id.etUserName);
        TextView passwordText = (TextView) findViewById(R.id.etPassword);

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));

        ((OkyLife) getApplication()).getMasterCaller().postData("User/loginUser", this, params);
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
