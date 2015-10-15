package com.example.okylifeapp.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by mordreth on 10/2/15.
 */
public class RegisterActivity extends Activity implements AsyncResponse {
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private boolean facebookButtomPresed = false;

    //AccountManager accountManager;
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        /**FACEBOOK**/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.register);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerWithFacebook();
            }
        });

        findViewById(R.id.sign_up_button_google).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerWithGoogle();
            }
        });

    }

    public void registerWithFacebook() {
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
                                    String sex = jobj.getString("gender");

                                    if (sex.equals("male")) {
                                        sex = "Male";
                                    } else {
                                        sex = "Female";
                                    }
                                    Log.v("RegisterActivity", sex);
                                    String firstName = jobj.getString("name");
                                    Log.v("RegisterActivity", firstName);
                                    String email = jobj.getString("email");
                                    Log.v("RegisterActivity", email);
                                    String id = jobj.getString("id");
                                    Log.v("RegisterActivity", id);
                                    String password = ((OkyLife) getApplication()).SHA1(id);
                                    Log.v("RegisterActivity", password);
                                    String birthday = jobj.getString("birthday");
                                    Log.v("RegisterActivity", birthday);
                                    String startDateString = birthday;
                                    String age = "0";
                                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                                    Date startDate;
                                    int mili = 315569;
                                    try {
                                        startDate = df.parse(startDateString);
                                        age = String.valueOf(((int) (startDate.getTime() / mili) / 100000) - 1);

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    Log.v("RegisterActivity", age);
                                    registerUser(sex, firstName, email, password, age);
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

    public void registerUser(String sex, String firstName, String email, String password, String age) {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("registerType", "Google"));
        params.add(new BasicNameValuePair("sex", sex));
        params.add(new BasicNameValuePair("firstName", firstName));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("age", age));
        ((OkyLife) getApplication()).getMasterCaller().postData("User/registerUser", this, params);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void processFinish(String response) {
        Toast.makeText(this, response, Toast.LENGTH_LONG).show();
        Log.v("response", response);
        if (facebookButtomPresed) {
            LoginManager.getInstance().logOut();
        }

    }

    public void registerWithGoogle() {
        Intent intent = new Intent(this, RegisterWithGoogleActivity.class);
        startActivity(intent);
    }

    public void register(View view) {
        /*Prueba para POST*/
        TextView emailText = (TextView) findViewById(R.id.etEmail);
        TextView passwordText = (TextView) findViewById(R.id.etPass);
        TextView userName = (TextView) findViewById(R.id.etUserName);
        Spinner sexSpinner = (Spinner) findViewById(R.id.spinner);
        EditText ageText = (EditText) findViewById(R.id.editText);

        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("email", emailText.getText().toString()));
        params.add(new BasicNameValuePair("password", passwordText.getText().toString()));
        params.add(new BasicNameValuePair("sex", sexSpinner.getSelectedItem().toString()));
        params.add(new BasicNameValuePair("age", ageText.getText().toString()));
        params.add(new BasicNameValuePair("firstName", userName.getText().toString()));
        params.add(new BasicNameValuePair("registerType", "Api"));

        ((OkyLife) getApplication()).getMasterCaller().postData("User/registerUser", this, params);

        /*Preba para GET*/
        //((okylife) getApplication()).getMasterCaller().getById("User", 1, this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, OkyLifeStartActivity.class);
        startActivity(intent);
        finish();
    }

}
