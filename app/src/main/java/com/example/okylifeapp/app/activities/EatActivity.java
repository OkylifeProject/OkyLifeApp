package com.example.okylifeapp.app.activities;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import aplication.OkyLife;
import com.example.okylifeapp.app.R;
import dialogs.LogoutDialog;
import rest.AsyncResponse;


/**
 * Created by Cristian Parada on 18/10/2015.
 */
public class EatActivity extends Activity implements AsyncResponse{

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.eat_activity);
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



    public void renderAddAlimentView(View view) {

        Intent saveActivityIntent = new Intent(this, AddAlimentActivity.class);
        finish();
        startActivity(saveActivityIntent);
    }

    public void renderAddFoodView(View view) {

        Intent saveActivityIntent = new Intent(this, AddFoodActivity.class);
        finish();
        startActivity(saveActivityIntent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
