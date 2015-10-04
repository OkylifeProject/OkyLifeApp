package com.example.okylifeapp.app.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import com.example.okylifeapp.app.R;


public class OkyLifeStartActivity extends ActionBarActivity {
    AccountManager accountManager;
    Account okyLifeAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.initial_view);
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
        /**ACCOUNT MANAGER **/
        accountManager = AccountManager.get(getApplicationContext());
        Account[] accounts = accountManager.getAccountsByType("com.example.okylifeapp.app");
        okyLifeAccount = null;
        //verify if exist any registered account
        if (accounts.length > 0) {
            okyLifeAccount = accounts[0];
        }
        if (okyLifeAccount != null) {

        }
    }

}
