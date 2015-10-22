package com.example.okylifeapp.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import com.example.okylifeapp.app.R;
import rest.AsyncResponse;


/**
 * Created by Cristian Parada on 18/10/2015.
 */
public class VisitPlaceActivity extends Activity implements AsyncResponse {

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.visit_place_activity);
    }

    @Override
    public void onStart() {
        super.onStart();



    }


    @Override
    public void processFinish(String result) {

    }




}
