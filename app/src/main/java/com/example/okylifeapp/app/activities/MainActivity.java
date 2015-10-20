package com.example.okylifeapp.app.activities;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import aplication.OkyLife;
import com.example.okylifeapp.app.R;
import data.User;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import rest.AsyncResponse;

import java.util.ArrayList;

/**
 * Created by mordreth on 10/4/15.
 */
public class MainActivity extends Activity implements AsyncResponse {
    private Account okyLifeAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
        okyLifeAccount = ((OkyLife) getApplication()).getOkyLifeAccount();
        Log.v("account", okyLifeAccount.name);


    }

    @Override
    public void onStart() {
        super.onStart();
        /** RETRIEVING USER INFO **/
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", String.valueOf(okyLifeAccount.name)));
        ((OkyLife) getApplication()).getMasterCaller().postData("User/getUserByEmail", this, params);
    }

    public void editProfile(View view) {
        Intent intent = new Intent(this, ProfileSettingsActivity.class);
        startActivity(intent);
    }
    public void startActivity(View view) {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {

    }

    @Override
    public void processFinish(String result) {
        Log.v("user", "arrived response");
        if (OkyLife.isJSON(result)) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                User user = new User(jsonObject.getString("firstName"),
                        jsonObject.getString("email"),
                        jsonObject.getString("registerType"),
                        jsonObject.getString("id"));

                if (jsonObject.has("age") && !jsonObject.isNull("age")) {
                    user.setAge(jsonObject.getString("age"));
                }
                if (jsonObject.has("imageBytes") && !jsonObject.isNull("imageBytes")) {
                    user.setImageBytes(jsonObject.getString("imageBytes"));
                }
                if (jsonObject.has("sex") && !jsonObject.isNull("sex")) {
                    user.setSex(jsonObject.getString("sex"));
                }
                ((OkyLife) getApplication()).setUser(user);
                Log.v("user", "Success");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), String.valueOf(result), Toast.LENGTH_LONG).show();
        }
    }
}
