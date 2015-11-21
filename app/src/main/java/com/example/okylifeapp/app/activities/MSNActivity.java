package com.example.okylifeapp.app.activities;

import android.accounts.Account;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import aplication.OkyLife;
import com.example.okylifeapp.app.R;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import rest.AsyncResponse;

import java.util.ArrayList;

/**
 * Created by mordreth on 11/17/15.
 */
public class MSNActivity extends Activity implements AsyncResponse {
    Account okyLifeAccount;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.msn_view);
        okyLifeAccount = ((OkyLife) getApplication()).getOkyLifeAccount();
    }

    public void sendMessage(View view) {
        EditText subjectTxt = (EditText) findViewById(R.id.subject_text);
        EditText msnTxt = (EditText) findViewById(R.id.msn_txt);
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("emailRemitent", okyLifeAccount.name));
        params.add(new BasicNameValuePair("emailRecipient", getIntent().getExtras().getString("emailRecipient")));
        params.add(new BasicNameValuePair("subject", subjectTxt.getText().toString()));
        params.add(new BasicNameValuePair("content", msnTxt.getText().toString()));

        ((OkyLife) getApplicationContext()).getMasterCaller().postData("Message/sendMessage", this, params);
    }

    public void cancelMessage(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void processFinish(String result) {
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
    }
}
