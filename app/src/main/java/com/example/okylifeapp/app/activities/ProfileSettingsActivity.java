package com.example.okylifeapp.app.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import aplication.OkyLife;
import com.example.okylifeapp.app.R;
import data.User;
import dialogs.LogoutDialog;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import rest.AsyncResponse;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by mordreth on 10/14/15.
 */
public class ProfileSettingsActivity extends Activity implements AsyncResponse, LogoutDialog.AlertPositiveLogoutListener {
    private static final int SELECT_PICTURE = 1;
    OkyLife app;
    byte[] imageBytes;
    AccountManager accountManager;
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

        /**ACCOUNT MANAGER **/
        accountManager = AccountManager.get(getApplicationContext());
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
        EditText weightText = (EditText) findViewById(R.id.weightText);
        EditText heightText = (EditText) findViewById(R.id.heightText);
        if (user != null) {
            ageText.setText(user.getAge());
            nameText.setText(user.getFirstName());
            if (user.getRegisterType().equals("Google") || user.getRegisterType().equals("Facebook")) {
                passwordText.setVisibility(View.GONE);
                password2Text.setVisibility(View.GONE);
            }
            weightText.setText(user.getWeight());
            heightText.setText(user.getHeight());
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
        Log.v("profile", result);
        if (result.equals("Success, password changed")) {
            OkyLife.deleteAccount(accountManager, okyLifeAccount);
        }
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
                selectedImage = OkyLife.decodeSampledBitmapFromUri(selectedImageStream, getContentResolver(), 200, 200);
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
        EditText weightText = (EditText) findViewById(R.id.weightText);
        EditText heightText = (EditText) findViewById(R.id.heightText);
        String email = okyLifeAccount.name;


        /** IMAGE **/
        Bitmap myImage = ((BitmapDrawable) myImageButton.getDrawable()).getBitmap();
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        myImage.compress(Bitmap.CompressFormat.JPEG, 95, bao);
        imageBytes = bao.toByteArray();

        double calories = OkyLife.calculateCaloriesHB(user.getSex(),
                Double.valueOf(ageText.getText().toString()),
                Double.valueOf(heightText.getText().toString()),
                Double.valueOf(weightText.getText().toString()), "Medium");

        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("image", Base64.encodeToString(imageBytes, Base64.DEFAULT)));
        params.add(new BasicNameValuePair("firstName", nameText.getText().toString()));
        params.add(new BasicNameValuePair("password1", password1Text.getText().toString()));
        params.add(new BasicNameValuePair("password2", password2Text.getText().toString()));
        params.add(new BasicNameValuePair("age", ageText.getText().toString()));
        params.add(new BasicNameValuePair("weight", weightText.getText().toString()));
        params.add(new BasicNameValuePair("height", heightText.getText().toString()));
        params.add(new BasicNameValuePair("calories", String.valueOf(calories)));

        ((OkyLife) getApplication()).getMasterCaller().postData("User/updateUserByEmail", this, params);
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
}
