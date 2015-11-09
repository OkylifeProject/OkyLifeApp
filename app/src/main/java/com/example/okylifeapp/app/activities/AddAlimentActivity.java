package com.example.okylifeapp.app.activities;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
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
public class AddAlimentActivity extends Activity implements AsyncResponse, LogoutDialog.AlertPositiveLogoutListener {

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_aliment_activity);
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

    public void renderDialogAlimentCancelConfirmation(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Atencion")
                .setMessage("Esta a punto de cancelar la adicion de un nuevo alimento. \nDesea Continuar?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        renderEatActivity();
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

    public void renderEatActivity() {
        Intent startActivityIntent = new Intent(this, EatActivity.class);
        startActivity(startActivityIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
