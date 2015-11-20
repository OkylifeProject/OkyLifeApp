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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import rest.AsyncResponse;

import java.util.ArrayList;

/**
 * Created by mordreth on 11/17/15.
 */
public class FriendsActivity extends Activity implements AsyncResponse {
    JSONArray jsonFriends;
    ArrayList<User> userFriends;
    private Account okyLifeAccount;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.friends_view);
        userFriends = new ArrayList<User>();
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
        setFriends(result);
    }

    private void setFriends(String result) {

        try {
            jsonFriends = new JSONArray(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String imageBytes;
        for (int i = 0; i < jsonFriends.length(); i++) {
            JSONObject friend = null;
            try {
                friend = (JSONObject) jsonFriends.get(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                imageBytes = friend.getString("imageBytes");
            } catch (JSONException e) {
                imageBytes = null;
            }
            try {
                userFriends.add(new User(friend.getString("name"),
                        imageBytes,
                        friend.getString("email"),
                        friend.getString("id")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.v("friends", String.valueOf(userFriends.size()));
    }


}
