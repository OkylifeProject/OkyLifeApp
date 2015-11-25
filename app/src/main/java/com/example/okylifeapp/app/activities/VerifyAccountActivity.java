package com.example.okylifeapp.app.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import aplication.OkyLife;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import rest.AsyncResponse;

import java.util.ArrayList;

/**
 * Created by mordreth on 11/25/15.
 */
public class VerifyAccountActivity extends Activity implements AsyncResponse {
    private AccountManager accountManager;
    private Account okyLifeAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /**ACCOUNT MANAGER **/
        accountManager = AccountManager.get(getApplicationContext());
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
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            ((OkyLife) getApplication()).setOkyLifeAccount(okyLifeAccount);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, OkyLifeStartActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
