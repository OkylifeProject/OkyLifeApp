package com.example.okylifeapp.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import com.example.okylifeapp.app.R;


public class OkyLifeStartActivity extends Activity {

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
    public void onBackPressed() {

    }
}
