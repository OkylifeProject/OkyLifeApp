package com.example.okylifeapp.app.activities;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import aplication.OkyLife;
import com.example.okylifeapp.app.R;

/**
 * Created by mordreth on 10/4/15.
 */
public class MainActivity extends Activity {
    private Account okyLifeAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
        okyLifeAccount = ((OkyLife) getApplication()).getOkyLifeAccount();
        Log.v("account", okyLifeAccount.name);
    }

    public void editProfile(View view) {
        Intent intent = new Intent(this, ProfileSettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}
