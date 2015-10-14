package com.example.okylifeapp.app.activities;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
 * Created by mordreth on 10/14/15.
 */
public class ProfileSettingsActivity extends Activity implements AsyncResponse {
    OkyLife app;
    private Account okyLifeAccount;
    private static final int SELECT_PICTURE = 1;
    private User user;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.edit_profile);
        okyLifeAccount = ((OkyLife) getApplication()).getOkyLifeAccount();
    }

    @Override
    public void onStart() {
        super.onStart();
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", String.valueOf(okyLifeAccount.name)));
        ((OkyLife) getApplication()).getMasterCaller().postData("User/getUserByEmail", this, params);
    }

    public void selectImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Selecciona Imagen"), SELECT_PICTURE);
    }

    public void setFields() {
        EditText nameText = (EditText) findViewById(R.id.firstNameText);
        EditText lastText = (EditText) findViewById(R.id.firstNameText);
        EditText passwordText = (EditText) findViewById(R.id.passwordText);
        EditText password2Text = (EditText) findViewById(R.id.password2Text);
        if (user != null) {
            nameText.setText(user.getFirstName());
            lastText.setText(user.getLastName());
            if (user.getRegisterType() == "Google" || user.getRegisterType() == "Facebook") {
                passwordText.setVisibility(View.INVISIBLE);
                password2Text.setVisibility(View.INVISIBLE);
            }
        }

    }

    @Override
    public void processFinish(String result) {
        Log.v("user", "arrived response");
        if (OkyLife.isJSON(result)) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                user = new User(jsonObject.getString("age"),
                        jsonObject.getString("registerType"),
                        jsonObject.getString("password"),
                        jsonObject.getString("email"),
                        jsonObject.getString("imageBytes"),
                        jsonObject.getString("sex"),
                        jsonObject.getString("lastName"),
                        jsonObject.getString("firstName"),
                        jsonObject.getString("id"));
                Log.v("user", "Succes");
                setFields();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), String.valueOf(result), Toast.LENGTH_LONG);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Bitmap yourSelectedImage = null;
                Uri selectedImageStream = data.getData();
                yourSelectedImage = app.decodeSampledBitmapFromUri(selectedImageStream, getContentResolver(), 200, 200);

                ImageButton photoSelectorButton = (ImageButton) findViewById(R.id.photoSelectorButton);
                photoSelectorButton.setImageBitmap(yourSelectedImage);
            }
        }
    }

    public void saveProfile(View view) {
        EditText nameText = (EditText) findViewById(R.id.firstNameText);
        EditText lastText = (EditText) findViewById(R.id.firstNameText);
        EditText passwordText = (EditText) findViewById(R.id.passwordText);
        EditText password2Text = (EditText) findViewById(R.id.password2Text);
        String email = okyLifeAccount.name;

    }
}
