package com.example.okylifeapp.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.Window;
import com.example.okylifeapp.app.R;
import rest.AsyncResponse;


/**
 * Created by Cristian Parada on 18/10/2015.
 */
public class EatActivity extends Activity implements AsyncResponse {

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.eat_activity);
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

}
