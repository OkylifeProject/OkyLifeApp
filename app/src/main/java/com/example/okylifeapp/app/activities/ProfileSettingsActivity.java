package com.example.okylifeapp.app.activities;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
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
import rest.AsyncResponse;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by mordreth on 10/14/15.
 */
public class ProfileSettingsActivity extends Activity implements AsyncResponse {
    private static final int SELECT_PICTURE = 1;
    OkyLife app;
    byte[] imageBytes;
    private Account okyLifeAccount;
    private User user;
    private Bitmap selectedImage;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.edit_profile);
        okyLifeAccount = ((OkyLife) getApplication()).getOkyLifeAccount();
        user = ((OkyLife) getApplication()).getUser();
    }

    @Override
    public void onStart() {
        super.onStart();
        setFields();
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
        EditText ageText = (EditText) findViewById(R.id.ageText);
        EditText passwordText = (EditText) findViewById(R.id.password1Text);
        EditText password2Text = (EditText) findViewById(R.id.password2Text);
        ImageButton photoSelectorButton = (ImageButton) findViewById(R.id.photoSelectorButton);
        if (user != null) {
            ageText.setText(user.getAge());
            nameText.setText(user.getFirstName());
            if (user.getRegisterType().equals("Google") || user.getRegisterType().equals("Facebook")) {
                passwordText.setVisibility(View.GONE);
                password2Text.setVisibility(View.GONE);
            }
        }
        if (selectedImage != null) {
            photoSelectorButton.setImageBitmap(selectedImage);
        } else if (user.getImageBytes() != null) {
            setProfileImage();
        }
    }

    @Override
    public void processFinish(String result) {
        Log.v("update", "arrived response");
        Toast.makeText(getApplicationContext(), String.valueOf(result), Toast.LENGTH_LONG).show();
    }

    public void setProfileImage() {
        byte[] imageBytes = Base64.decode(user.getImageBytes(), Base64.DEFAULT);
        ImageButton myImageButton = (ImageButton) findViewById(R.id.photoSelectorButton);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        myImageButton.setImageBitmap(bitmap);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageStream = data.getData();
                selectedImage = app.decodeSampledBitmapFromUri(selectedImageStream, getContentResolver(), 200, 200);
            }
        }
    }

    public void saveProfile(View view) {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        ImageButton myImageButton = (ImageButton) findViewById(R.id.photoSelectorButton);
        EditText nameText = (EditText) findViewById(R.id.firstNameText);
        EditText password1Text = (EditText) findViewById(R.id.password1Text);
        EditText password2Text = (EditText) findViewById(R.id.password2Text);
        EditText ageText = (EditText) findViewById(R.id.ageText);
        String email = okyLifeAccount.name;


        /** IMAGE **/
        Bitmap myImage = ((BitmapDrawable) myImageButton.getDrawable()).getBitmap();
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        myImage.compress(Bitmap.CompressFormat.JPEG, 95, bao);
        imageBytes = bao.toByteArray();

        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("image", Base64.encodeToString(imageBytes, Base64.DEFAULT)));
        params.add(new BasicNameValuePair("firstName", nameText.getText().toString()));
        params.add(new BasicNameValuePair("password1", password1Text.getText().toString()));
        params.add(new BasicNameValuePair("password2", password2Text.getText().toString()));
        params.add(new BasicNameValuePair("age", ageText.getText().toString()));

        ((OkyLife) getApplication()).getMasterCaller().postData("User/updateUserByEmail", this, params);
    }
}
