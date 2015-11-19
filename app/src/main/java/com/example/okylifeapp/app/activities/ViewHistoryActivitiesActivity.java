package com.example.okylifeapp.app.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import aplication.OkyLife;
import com.example.okylifeapp.app.R;
import dialogs.LogoutDialog;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import rest.AsyncResponse;

import java.util.ArrayList;


/**
 * Created by Cristian Parada on 18/10/2015.
 */
public class ViewHistoryActivitiesActivity extends Activity implements AsyncResponse, LogoutDialog.AlertPositiveLogoutListener {
    private Account okyLifeAccount;
    String[] nameActivity;
    String[] creationDate;
    int arr_images[];
    ListView listHistoryActivities;
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_history_activities_activity);
        listHistoryActivities = (ListView)findViewById(R.id.listActivities);
        okyLifeAccount = ((OkyLife) getApplication()).getOkyLifeAccount();
        getNotesUsers();
    }
    public void getNotesUsers(){
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", okyLifeAccount.name));
        ((OkyLife) getApplication()).getMasterCaller().postData("Activity/getUserActivities", this, params);
    }

    public void publicHistoryActivities(JSONArray arrayActivities){
        nameActivity = new String[arrayActivities.length()];
        creationDate = new String[arrayActivities.length()];
        arr_images = new int[arrayActivities.length()];
        for (int i = 0; i <arrayActivities.length() ; i++) {
            try {
                JSONObject note = (JSONObject) arrayActivities.get(i);
                nameActivity[i] = note.getString("name");
                creationDate[i] = note.getString("creationDate");
                arr_images[i] = R.drawable.activity_icon;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        listHistoryActivities.setAdapter(new MyAdapter(this, R.layout.row, nameActivity));
    }

    @Override
    public void onStart() {
        super.onStart();

    }


    @Override
    public void processFinish(String result) {
        if (OkyLife.isJSON(result)) {
            try {
                JSONArray arrayNotes = new JSONArray(result);
                publicHistoryActivities(arrayNotes);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public class MyAdapter extends ArrayAdapter<String> {

        public MyAdapter(Context context, int textViewResourceId, String[] objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.row, parent, false);
            TextView label = (TextView) row.findViewById(R.id.company);
            label.setText(nameActivity[position]);

            TextView sub = (TextView) row.findViewById(R.id.sub);
            sub.setText(creationDate[position]);

            ImageView icon = (ImageView) row.findViewById(R.id.image);
            icon.setImageResource(arr_images[position]);

            return row;
        }
    }
}
