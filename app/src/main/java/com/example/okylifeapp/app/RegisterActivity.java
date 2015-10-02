package com.example.okylifeapp.app;

import android.app.Activity;
import android.os.Bundle;
import rest.AsyncResponse;

/**
 * Created by mordreth on 10/2/15.
 */
public class RegisterActivity extends Activity implements AsyncResponse {
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.register);
    }

    @Override
    public void processFinish(String result) {

    }
}
