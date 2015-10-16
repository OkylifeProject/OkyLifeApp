package com.example.okylifeapp.app.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import aplication.OkyLife;
import com.example.okylifeapp.app.R;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import rest.AsyncResponse;

import java.util.ArrayList;


public class OkyLifeStartActivity extends ActionBarActivity implements AsyncResponse {
    AccountManager accountManager;
    Account okyLifeAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.initial_view);

        /**ACCOUNT MANAGER **/
        accountManager = AccountManager.get(getApplicationContext());

    }

    public void renderRegisterView(View view) {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        finish();
        startActivity(registerIntent);
    }

    public void renderLoginView(View view) {
        Intent registerIntent = new Intent(this, LoginActivity.class);
        finish();
        startActivity(registerIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        /**VERIFY REGISTERED ACCOUNT**/
        Account[] accounts = accountManager.getAccountsByType("com.example.okylifeapp.app");
        okyLifeAccount = null;

        //verify if exist any registered account
        if (accounts.length > 0) {
            okyLifeAccount = accounts[0];
        }
        if (okyLifeAccount != null) {
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", okyLifeAccount.name));
            params.add(new BasicNameValuePair("password", accountManager.getPassword(okyLifeAccount)));
            loginUserRequest(params);

        }

    }

    private void loginUserRequest(ArrayList<NameValuePair> params) {
        ((OkyLife) getApplication()).getMasterCaller().postData("User/loginUser", this, params);
    }

    @Override
    public void processFinish(String result) {
        if (OkyLife.isJSON(result)) {
            ((OkyLife) getApplication()).setOkyLifeAccount(okyLifeAccount);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onBackPressed() {

    }
}
