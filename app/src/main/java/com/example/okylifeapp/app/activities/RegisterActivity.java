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
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import rest.AsyncResponse;

import java.util.ArrayList;

/**
 * Created by mordreth on 10/2/15.
 */
public class RegisterActivity extends Activity implements AsyncResponse {

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register);

        findViewById(R.id.sign_up_button_google).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerWithGoogle();
            }
        });

    }

    @Override
    public void processFinish(String response) {
        Toast.makeText(this, response, Toast.LENGTH_LONG).show();
        Log.v("response", response);
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

        ((OkyLife) getApplication()).getMasterCaller().postData("User/registerUser", this, params);

        /*Preba para GET*/
        //((OkyLife) getApplication()).getMasterCaller().getById("User", 1, this);
    }

}
