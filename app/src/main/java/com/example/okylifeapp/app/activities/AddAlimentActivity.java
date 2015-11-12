package com.example.okylifeapp.app.activities;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.EditText;
import android.widget.Spinner;
import aplication.OkyLife;
import com.example.okylifeapp.app.R;
import dialogs.LogoutDialog;
import org.json.JSONException;
import org.json.JSONObject;
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
        Intent startActivityIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, startActivityIntent);
        finish();
    }

    public void returnAlimentToEatActivity(View view) {
        JSONObject jsonAliment = new JSONObject();

        EditText titleText = (EditText) findViewById(R.id.aliment_title);
        EditText descriptionText = (EditText) findViewById(R.id.aliment_description);
        EditText rationSizeText = (EditText) findViewById(R.id.aliment_size_ration);
        Spinner rationTypeSpinner = (Spinner) findViewById(R.id.aliment_spinnerTypeRation);
        EditText caloriesText = (EditText) findViewById(R.id.aliment_calories);
        EditText fatText = (EditText) findViewById(R.id.aliment_total_fat);
        EditText carbohydratesText = (EditText) findViewById(R.id.aliment_total_carbohydrates);
        EditText proteinsText = (EditText) findViewById(R.id.aliment_protein);

        try {
            jsonAliment.put("name", titleText.getText().toString());
            jsonAliment.put("description", descriptionText.getText().toString());
            jsonAliment.put("number", rationSizeText.getText().toString());
            jsonAliment.put("fat", fatText.getText().toString());
            jsonAliment.put("carbohydrates", carbohydratesText.getText().toString());
            jsonAliment.put("proteins", proteinsText.getText().toString());
            jsonAliment.put("calories", caloriesText.getText().toString());
            jsonAliment.put("rationType", String.valueOf(rationTypeSpinner.getSelectedItem()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent startActivityIntent = new Intent();
        startActivityIntent.putExtra("jsonAliment", jsonAliment.toString());
        setResult(Activity.RESULT_OK, startActivityIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
