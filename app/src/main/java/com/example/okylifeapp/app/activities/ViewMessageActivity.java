package com.example.okylifeapp.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.example.okylifeapp.app.R;

/**
 * Created by mordreth on 11/20/15.
 */
public class ViewMessageActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.message_view);
        setFields();
    }

    private void setFields() {
        TextView remitentText = (TextView) findViewById(R.id.remitent_name_show);
        TextView subjectText = (TextView) findViewById(R.id.subject_name_show);
        TextView msnText = (TextView) findViewById(R.id.msn_content_show);

        remitentText.setText(getIntent().getExtras().getString("remitent"));
        subjectText.setText(getIntent().getExtras().getString("subject"));
        msnText.setText(getIntent().getExtras().getString("content"));
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void accept(View view) {
        finish();
    }
}
