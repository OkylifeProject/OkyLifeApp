package com.example.okylifeapp.app.activites;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.okylifeapp.app.R;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import rest.AsyncResponse;
import rest.RequestCaller;

import java.util.ArrayList;

/**
 * Created by mordreth on 10/2/15.
 */
public class RegisterActivity extends Activity implements AsyncResponse {
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.register);
    }

    @Override
    public void processFinish(String result) {
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG);
    }

    public void register(View view) {
        RequestCaller requestCaller = new RequestCaller();

        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("registerType", "Api"));
        params.add(new BasicNameValuePair("sex", "Male"));
        params.add(new BasicNameValuePair("fisrtName", "Alejo"));
        params.add(new BasicNameValuePair("lastName", "Velasco"));
        params.add(new BasicNameValuePair("password", "Lanzarote222"));
        params.add(new BasicNameValuePair("email", "savelascod@gmail.com"));
        params.add(new BasicNameValuePair("birthDate", "5/3/1994"));

        requestCaller.postData("User/rgisterUser", this, params);
    }
}
