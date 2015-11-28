package com.example.okylifeapp.app.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
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
public class ViewNewsActivity extends Activity implements AsyncResponse, LogoutDialog.AlertPositiveLogoutListener, ShowDialogNote.AlertPositiveExitListener {
    private Account okyLifeAccount;
    String[] contentNotes;
    String[] namePersons;
    String[] date;
    Bitmap arr_images[];
    ListView listNews;
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_news_activity);
        listNews = (ListView)findViewById(R.id.listNews);
        okyLifeAccount = ((OkyLife) getApplication()).getOkyLifeAccount();
        getNotesUsers();
        listNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                showDialogNote(position);
                //Toast.makeText(getApplicationContext(),"Ha pulsado el item " + position, Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void showDialogNote(int position){
        FragmentManager manager = getFragmentManager();
        ShowDialogNote showDetailsNote = new ShowDialogNote(getApplicationContext(),namePersons[position],contentNotes[position],date[position],arr_images[position]);
        showDetailsNote.show(manager, "show");

    }
    public void getNotesUsers(){
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", okyLifeAccount.name));
        ((OkyLife) getApplication()).getMasterCaller().postData("Note/getRecentNotesByUser", this, params);
    }

    public void publicNotes(JSONArray arrayNotes){
        namePersons = new String[arrayNotes.length()];
        contentNotes = new String[arrayNotes.length()];
        date = new String[arrayNotes.length()];
        arr_images = new Bitmap[arrayNotes.length()];
        for (int i = 0; i <arrayNotes.length() ; i++) {
            try {
                JSONObject note = (JSONObject) arrayNotes.get(i);
                namePersons[i] = note.getString("ownerEmail");
                contentNotes[i] = note.getString("content");
                date[i] = note.getString("publicationDate");
                if(note.getString("ownerImageBytes").equals("")){
                    Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.default_image);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
                    arr_images[i] = bitmap;
                }
                else{
                    byte[] imageBytes = Base64.decode((note.getString("ownerImageBytes")), Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
                    arr_images[i] = bitmap;

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

    @Override
    public void onPositiveExitClick(boolean exit) {

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
            label.setText(namePersons[position]);

            TextView sub = (TextView) row.findViewById(R.id.sub);
            if(contentNotes[position].length()>50){
                sub.setText(contentNotes[position].substring(0,50)+"...");
            }
            else{
                sub.setText(contentNotes[position]);
            }

            ImageView icon = (ImageView) row.findViewById(R.id.image);
            icon.setImageBitmap(arr_images[position]);

            return row;
        }
    }

}
