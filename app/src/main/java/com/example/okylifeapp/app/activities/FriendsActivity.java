package com.example.okylifeapp.app.activities;

import android.accounts.Account;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import aplication.OkyLife;
import com.example.okylifeapp.app.R;
import data.User;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import rest.AsyncResponse;

import java.util.ArrayList;

/**
 * Created by mordreth on 11/17/15.
 */
public class FriendsActivity extends Activity implements AsyncResponse {
    ArrayList<User> friends;
    private Account okyLifeAccount;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.friends_view);
        okyLifeAccount = ((OkyLife) getApplication()).getOkyLifeAccount();
        getFriends();
    }

    private void getFriends() {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", okyLifeAccount.name));
        ((OkyLife) getApplication()).getMasterCaller().postData("User/getFriends", this, params);
    }


    @Override
    public void processFinish(String result) {
        Log.v("userFriends", result);
    }
}
