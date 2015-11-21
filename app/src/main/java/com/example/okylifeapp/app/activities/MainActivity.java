package com.example.okylifeapp.app.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import aplication.OkyLife;
import com.example.okylifeapp.app.R;
import data.User;
import dialogs.LogoutDialog;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import rest.AsyncResponse;

import java.util.ArrayList;

/**
 * Created by mordreth on 10/4/15.
 */
public class MainActivity extends Activity implements AsyncResponse, LogoutDialog.AlertPositiveLogoutListener {
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

    public void writeNote(View view) {
        Intent intent = new Intent(this, WriteNoteActivity.class);
        startActivity(intent);
    }

    public void startActivity(View view) {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }

    public void viewFriends(View view) {
        Intent intent = new Intent(this, FriendsActivity.class);
        startActivity(intent);
    }

    public void viewNews(View view) {
        Intent intent = new Intent(this, ViewNewsActivity.class);
        startActivity(intent);
    }

    public void viewInbox(View view) {
        Intent intent = new Intent(this, InboxActivity.class);
        startActivity(intent);
    }

    public void viewHistoryActivities(View view) {
        Intent intent = new Intent(this, ViewHistoryActivitiesActivity.class);
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
                if (jsonObject.has("height") && !jsonObject.isNull("height")) {
                    user.setHeight(jsonObject.getString("height"));
                }
                if (jsonObject.has("weight") && !jsonObject.isNull("weight")) {
                    user.setWeight(jsonObject.getString("weight"));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_oky_life, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                showLogoutDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showLogoutDialog() {
        FragmentManager manager = getFragmentManager();
        LogoutDialog logoutDialog = new LogoutDialog();
        logoutDialog.show(manager, "Logout");
    }

    @Override
    public void onPositiveLogoutClick(boolean logout) {
        OkyLife.deleteAccount(AccountManager.get(getApplicationContext()), ((OkyLife) getApplication()).getOkyLifeAccount());
        Intent intent = new Intent(this, OkyLifeStartActivity.class);
        startActivity(intent);
        finish();
    }
}
