package com.example.okylifeapp.app.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import aplication.OkyLife;
import com.example.okylifeapp.app.R;
import rest.AsyncResponse;

/**
 * Created by Cristian Parada on 03/10/2015.
 */
public class LoginActivity extends Activity implements AsyncResponse {
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
    }

    @Override
    public void processFinish(String response) {
        Toast.makeText(this, response, Toast.LENGTH_LONG).show();
        Log.v("response", response);
    }

    public void login(View view) {
        showDialog(0);
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Create the quit confirmation dialog

        builder.setMessage("en Construccion...");
        dialog = builder.create();

        return dialog;
    }

}
