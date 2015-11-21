package com.example.okylifeapp.app.activities;

import android.app.Activity;
import android.os.Bundle;
import com.example.okylifeapp.app.R;
import rest.AsyncResponse;

/**
 * Created by mordreth on 11/20/15.
 */
public class InboxActivity extends Activity implements AsyncResponse {
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.inbox_view);
    }

    @Override
    public void processFinish(String result) {

    }
}
