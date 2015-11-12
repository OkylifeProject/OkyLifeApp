package com.example.okylifeapp.app.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.okylifeapp.app.R;
import dialogs.LogoutDialog;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import rest.AsyncResponse;

import java.util.ArrayList;


/**
 * Created by Cristian Parada on 18/10/2015.
 */
public class EatActivity extends Activity implements AsyncResponse {

    static final int ADD_ALIMENT_REQUEST = 1;

    JSONArray jsonAliments;
    String[] nameAliments;
    String[] descriptionAliments;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.eat_activity);
        jsonAliments = new JSONArray();

        Intent intent = new Intent(this, getLocationActivity.class);
        startActivity(intent);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                showLogoutDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showLogoutDialog() {
        FragmentManager manager = getFragmentManager();
        LogoutDialog logoutDialog = new LogoutDialog();
        logoutDialog.show(manager, "Logout");
    }


    public void renderAddAlimentView(View view) {
        Intent addAliment = new Intent(getApplicationContext(), AddAlimentActivity.class);
        startActivityForResult(addAliment, ADD_ALIMENT_REQUEST);
    }

    public void saveEatActivity(View view) {
        //TODO add the default EAT acitivity fields
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("ingredients", jsonAliments.toString()));


    }

    /*
    public void renderAddFoodView(View view) {

        Intent saveActivityIntent = new Intent(this, AddFoodActivity.class);
        finish();
        startActivity(saveActivityIntent);
    }
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_ALIMENT_REQUEST) {
            if (resultCode == RESULT_OK) {
                try {
                    JSONObject jsonAliment = new JSONObject(data.getStringExtra("jsonAliment"));
                    jsonAliments.put(jsonAliment);
                    showAliments();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void showAliments() {
        ListView alimentList = (ListView) findViewById(R.id.listAliments);
        nameAliments = new String[jsonAliments.length()];
        descriptionAliments = new String[jsonAliments.length()];

        for (int i = 0; i < jsonAliments.length(); i++) {
            try {
                JSONObject aliment = (JSONObject) jsonAliments.get(i);
                nameAliments[i] = aliment.getString("name");
                descriptionAliments[i] = aliment.getString("description");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        alimentList.setAdapter(new MyAdapter(this, R.layout.row, nameAliments));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public class MyAdapter extends ArrayAdapter<String> {

        public MyAdapter(Context context, int textViewResourceId, String[] objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.aliment_row, parent, false);
            TextView nameText = (TextView) row.findViewById(R.id.aliment_name);
            TextView descriptionText = (TextView) row.findViewById(R.id.aliment_description);
            nameText.setText(nameAliments[position]);
            descriptionText.setText(descriptionAliments[position]);
            return row;
        }
    }
}
