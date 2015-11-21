package com.example.okylifeapp.app.activities;

import android.accounts.Account;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import aplication.OkyLife;
import com.example.okylifeapp.app.R;
import data.Message;
import data.MyMessageAdapter;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import rest.AsyncResponse;

import java.util.ArrayList;

/**
 * Created by mordreth on 11/20/15.
 */
public class InboxActivity extends Activity implements AsyncResponse {
    ArrayList<Message> messages;
    private Account okyLifeAccount;
    private ListView msnList;
    private MyMessageAdapter messageAdapter;
    private JSONArray jsonMessages;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.inbox_view);
        messages = new ArrayList<Message>();
        okyLifeAccount = ((OkyLife) getApplication()).getOkyLifeAccount();
        msnList = (ListView) findViewById(R.id.msn_list);
        getMessages();
    }

    private void getMessages() {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", okyLifeAccount.name));
        ((OkyLife) getApplication()).getMasterCaller().postData("Message/getMessagesByUser", this, params);
    }

    @Override
    public void processFinish(String result) {
        setMessages(result);
        setListMessageAdapter();
    }

    private void setListMessageAdapter() {
// instantiate our ItemAdapter class
        messageAdapter = new MyMessageAdapter(this, R.layout.message_row, messages);
        msnList.setAdapter(messageAdapter);
    }

    private void setMessages(String result) {
        try {
            jsonMessages = new JSONArray(result);
            if (jsonMessages != null) {
                for (int i = 0; i < jsonMessages.length(); i++) {
                    JSONObject message = (JSONObject) (jsonMessages.get(i));
                    messages.add(new Message(
                            message.getString("id"),
                            message.getString("remitentEmail"),
                            message.getString("subject"),
                            message.getString("recipientEmail"),
                            message.getString("content")
                    ));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
