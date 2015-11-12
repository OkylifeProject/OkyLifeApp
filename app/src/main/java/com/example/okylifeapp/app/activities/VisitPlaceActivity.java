package com.example.okylifeapp.app.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import aplication.OkyLife;
import com.example.okylifeapp.app.R;
import dialogs.LogoutDialog;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import rest.AsyncResponse;

import java.util.ArrayList;


/**
 * Created by Cristian Parada on 18/10/2015.
 */
public class VisitPlaceActivity extends Activity implements AsyncResponse, LogoutDialog.AlertPositiveLogoutListener {
    Spinner typePlace;
    String[] strings = {"Park", "Museum", "Store", "Hospital", "Restaurant"};

    String[] subs = {"", "", "", "", ""};

    int arr_images[] = {R.drawable.park,
            R.drawable.museum,
            R.drawable.mall,
            R.drawable.hospital,
            R.drawable.restaurant};
    double distance;
    private Account okyLifeAccount;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.visit_place_activity);
        okyLifeAccount = ((OkyLife) getApplication()).getOkyLifeAccount();

        typePlace = (Spinner) findViewById(R.id.spinnerTypePlace);
        typePlace.setAdapter(new MyAdapter(this, R.layout.row, strings));

        distance = 0;
    }

    public void renderDialogPlaceCancelConfirmation(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Atencion")
                .setMessage("Esta a punto de borrar el registro de la actividad realizada. \nDesea Continuar?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        renderStartActivity();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void saveVisitPlaceActivity(View view) {
        EditText titleText = (EditText) findViewById(R.id.vp_title_text);
        EditText descriptionText = (EditText) findViewById(R.id.vp_description_text);
        Spinner spinner = (Spinner) findViewById(R.id.spinnerTypePlace);
        EditText addressText = (EditText) findViewById(R.id.vp_address_text);
        EditText caloriesText = (EditText) findViewById(R.id.vp_calories_text);

        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", okyLifeAccount.name));
        params.add(new BasicNameValuePair("name", titleText.getText().toString()));
        params.add(new BasicNameValuePair("description", descriptionText.getText().toString()));

        params.add(new BasicNameValuePair("type", strings[spinner.getSelectedItemPosition()]));
        params.add(new BasicNameValuePair("address", addressText.getText().toString()));
        params.add(new BasicNameValuePair("calories", caloriesText.getText().toString()));
        params.add(new BasicNameValuePair("distance", String.valueOf(distance)));
    }

    public void renderStartActivity() {
        Intent startActivityIntent = new Intent(this, StartActivity.class);
        finish();
        startActivity(startActivityIntent);

    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void processFinish(String result) {

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
            label.setText(strings[position]);

            TextView sub = (TextView) row.findViewById(R.id.sub);
            sub.setText(subs[position]);

            ImageView icon = (ImageView) row.findViewById(R.id.image);
            icon.setImageResource(arr_images[position]);

            return row;
        }
    }

}
