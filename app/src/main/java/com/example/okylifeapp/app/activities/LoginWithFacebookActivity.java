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
import com.facebook.*;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import rest.AsyncResponse;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Cristian Parada on 03/10/2015.
 */
public class LoginWithFacebookActivity extends Activity implements AsyncResponse {
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
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));
        loginButton.setBackgroundResource(R.drawable.logo);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                /* make the API call */
                GraphRequest request = new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/" + AccessToken.getCurrentAccessToken().getUserId(),
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                //Log.v("LoginActivity", response.toString());
                                try {
                                    JSONObject jobj = new JSONObject(response.getRawResponse());
                                    String email = jobj.getString("email");
                                    //Log.v("LoginActivity",email);
                                    String user = jobj.getString("name");
                                    //Log.v("LoginActivity",user);
                                    String id = jobj.getString("id");
                                    //Log.v("LoginActivity",id);
                                    String password = ((OkyLife) getApplication()).SHA1(id);
                                    //Log.v("LoginActivity", password);
                                    verifyLoggin(email, password);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                );
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,birthday,gender");
                request.setParameters(parameters);
                request.executeAsync();
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

        /**ACCOUNT MANAGER **/
        accountManager = AccountManager.get(getApplicationContext());

    }

    public void verifyLoggin(String email, String password){
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password",password));
        ((OkyLife) getApplication()).getMasterCaller().postData("User/loginUser", this, params);
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
}
