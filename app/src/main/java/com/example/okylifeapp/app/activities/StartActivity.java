package com.example.okylifeapp.app.activities;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import aplication.OkyLife;
import com.example.okylifeapp.app.R;
import rest.AsyncResponse;


/**
 * Created by Cristian Parada on 18/10/2015.
 */
public class StartActivity extends Activity implements AsyncResponse {
    ListView list;
    String[] typeActivities = {
            "Comer",
            "Hacer Deporte",
            "Visitar Un lugar"
    } ;
    Integer[] imageId = {
            R.drawable.eat,
            R.drawable.doing_sports,
            R.drawable.visit_place
    };

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.select_type_activity);
        TypeActivity adapter = new TypeActivity (this, typeActivities, imageId);
        list=(ListView)findViewById(R.id.listTypeActivity);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selectActivity(position);
                //Toast.makeText(getApplicationContext(),"Ha pulsado el item " + position, Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void processFinish(String result) {

    }

    public void selectActivity(int itemSelect){
        Intent intent;
        if(itemSelect==0){
            intent = new Intent(this, EatActivity.class);
        }
        else if(itemSelect==1){
            intent = new Intent(this, DoingSportActivity.class);
        }
        else{
            intent = new Intent(this, VisitPlaceActivity.class);
        }
        startActivity(intent);
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
                OkyLife.deleteAccount(AccountManager.get(getApplicationContext()), ((OkyLife) getApplication()).getOkyLifeAccount());
                Intent intent = new Intent(this, OkyLifeStartActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
