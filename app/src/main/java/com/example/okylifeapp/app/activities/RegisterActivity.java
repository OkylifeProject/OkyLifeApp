package com.example.okylifeapp.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
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

    }

    @Override
    public void processFinish(String response) {
        Toast.makeText(this, response, Toast.LENGTH_LONG).show();
        Log.v("response", response);
    }

    public void registerWithGoogle(View view) {
        Intent intent = new Intent(this, RegisterWithGoogleActivity.class);
        startActivity(intent);
    }

    public void register(View view) {
        /*Prueba para POST*/

        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("registerType", "Api"));
        params.add(new BasicNameValuePair("sex", "Male"));
        params.add(new BasicNameValuePair("firstName", "Alejo"));
        params.add(new BasicNameValuePair("lastName", "Velasco"));
        params.add(new BasicNameValuePair("password", "Lanzarote222"));
        params.add(new BasicNameValuePair("email", "savelascod@gmail.com"));
        params.add(new BasicNameValuePair("birthDate", "5/3/1994"));

        ((OkyLife) getApplication()).getMasterCaller().postData("User/registerUser", this, params);

        /*Preba para GET*/
        //((OkyLife) getApplication()).getMasterCaller().getById("User", 1, this);
    }

}
