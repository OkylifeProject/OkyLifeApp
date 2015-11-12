package com.example.okylifeapp.app.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import aplication.OkyLife;
import com.example.okylifeapp.app.R;
import com.facebook.login.LoginManager;
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
public class ViewNewsActivity extends Activity implements AsyncResponse, LogoutDialog.AlertPositiveLogoutListener {
    private Account okyLifeAccount;
    String[] contentNotes;
    String[] date;
    int arr_images[];
    ListView listNews;
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_news_activity);
        listNews = (ListView)findViewById(R.id.listNews);
        okyLifeAccount = ((OkyLife) getApplication()).getOkyLifeAccount();
        getNotesUsers();
    }
    public void getNotesUsers(){
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", okyLifeAccount.name));
        ((OkyLife) getApplication()).getMasterCaller().postData("Note/getNotesByUser", this, params);
    }

    public void publicNotes(JSONArray arrayNotes){
        contentNotes = new String[arrayNotes.length()];
        date = new String[arrayNotes.length()];
        arr_images = new int[arrayNotes.length()];
        for (int i = 0; i <arrayNotes.length() ; i++) {
            try {
                JSONObject note = (JSONObject) arrayNotes.get(i);
                contentNotes[i] = note.getString("content");
                date[i] = note.getString("publicationDate");
                if(note.getString("imageHash").equals("")){
                    arr_images[i] = R.drawable.default_image_40_40;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        listNews.setAdapter(new MyAdapter(this, R.layout.row, contentNotes));
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
                publicNotes(arrayNotes);
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
            label.setText(contentNotes[position]);

            TextView sub = (TextView) row.findViewById(R.id.sub);
            sub.setText(date[position]);

            ImageView icon = (ImageView) row.findViewById(R.id.image);
            icon.setImageResource(arr_images[position]);

            return row;
        }
    }
}
