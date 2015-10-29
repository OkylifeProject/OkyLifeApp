package com.example.okylifeapp.app.activities;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import aplication.OkyLife;
import com.example.okylifeapp.app.R;
import com.facebook.*;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import rest.AsyncResponse;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Cristian Parada on 03/10/2015.
 */
public class LoginActivity extends Activity implements AsyncResponse {
    AccountManager accountManager;
    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private boolean facebookButtomPresed = false;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        /**FACEBOOK**/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.login);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithFacebook();
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

    public void loginWithFacebook() {
        facebookButtomPresed = true;
        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));
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
                                try {
                                    JSONObject jobj = new JSONObject(response.getRawResponse());
                                    String email = jobj.getString("email");
                                    Log.v("LoginActivity", email);
                                    String user = jobj.getString("name");
                                    Log.v("LoginActivity", user);
                                    String id = jobj.getString("id");
                                    Log.v("LoginActivity", id);
                                    String password = ((OkyLife) getApplication()).SHA1(id);
                                    Log.v("LoginActivity", password);
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

            }

            @Override
            public void onError(FacebookException e) {

            }
        });
    }

    public void verifyLoggin(String email, String password) {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        ((OkyLife) getApplication()).getMasterCaller().postData("User/loginUser", this, params);
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
                Intent intent = new Intent(this, OkyLifeStartActivity.class);
                finish();
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            if (facebookButtomPresed) {
                LoginManager.getInstance().logOut();
            }
            Toast.makeText(this, response, Toast.LENGTH_LONG).show();

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
    public void onBackPressed() {
        Intent intent = new Intent(this, OkyLifeStartActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_oky_life, menu);
        return true;
    }
}
