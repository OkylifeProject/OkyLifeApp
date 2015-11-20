package com.example.okylifeapp.app.activities;

import android.accounts.Account;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import aplication.OkyLife;
import com.example.okylifeapp.app.R;
import data.MyFriendAdapter;
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
    JSONArray jsonUsers;
    ArrayList<User> users;
    private Account okyLifeAccount;
    private ListView friendList;
    private MyFriendAdapter serviceAdapter;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.friends_view);
        users = new ArrayList<User>();
        okyLifeAccount = ((OkyLife) getApplication()).getOkyLifeAccount();
        friendList = (ListView) findViewById(R.id.friend_list);
        //setListUserAdapter();
        getFriends();
    }

    private void getFriends() {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", okyLifeAccount.name));
        ((OkyLife) getApplication()).getMasterCaller().postData("User/getFriends", this, params);
    }


    @Override
    public void processFinish(String result) {
        setUsers(result);
        setListUserAdapter();
    }

    private void updateListAdapter() {
        ((BaseAdapter) friendList.getAdapter()).notifyDataSetChanged();
    }

    private void setListUserAdapter() {
        // instantiate our ItemAdapter class
        serviceAdapter = new MyFriendAdapter(this, R.layout.friend_row, users);
        friendList.setAdapter(serviceAdapter);
    }


    private void setUsers(String result) {
        users = new ArrayList<User>();
        try {
            jsonUsers = new JSONArray(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String imageBytes;
        if (jsonUsers != null) {
            for (int i = 0; i < jsonUsers.length(); i++) {
                JSONObject user = null;
                try {
                    user = (JSONObject) jsonUsers.get(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    imageBytes = user.getString("imageBytes");
                } catch (JSONException e) {
                    imageBytes = null;
                }
                try {
                    users.add(new User(user.getString("name"),
                            imageBytes,
                            user.getString("email"),
                            user.getString("id"),
                            user.getBoolean("isFriend")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void getUsersByName(String userName) {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", okyLifeAccount.name));
        params.add(new BasicNameValuePair("userName", userName));
        ((OkyLife) getApplication()).getMasterCaller().postData("User/getUsersByName", this, params);
    }

    public void searchUsers(View view) {
        EditText entryText = (EditText) findViewById(R.id.search_friend_box);
        if (entryText.getText().toString().equals("")) {
            getFriends();
        } else {
            getUsersByName(entryText.getText().toString());
        }

    }
}
