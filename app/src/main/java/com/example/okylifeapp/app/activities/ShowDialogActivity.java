package com.example.okylifeapp.app.activities;

import android.accounts.Account;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import aplication.OkyLife;
import com.example.okylifeapp.app.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.plus.Plus;
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
public class ShowDialogActivity   extends Activity implements AsyncResponse{
    private static String SPORT = "okylifeapi.SportActivity";
    private static String EAT = "okylifeapi.EatActivity";
    private static String VISIT_PLACE = "okylifeapi.VisitPlaceActivity";
    private String classActivity;
    private String idActivity;
    private String[] i_name,i_description,i_calories,i_carbohydrates,i_fat,i_proteins,i_number,i_rationType;
    private TextView name,date,description,total_proteins,total_calories,total_carbohydrates,total_fat,total_distance,address,
            total_hydration,total_duration, total_rhythm,total_velocity;
    private ListView listIngredients;
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        classActivity = getIntent().getExtras().getString("class");
        idActivity = getIntent().getExtras().getString("id");
        Log.v("id recibido",idActivity);
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id", idActivity));
        if(classActivity.equals(SPORT)){
            Log.v("Es un Sport:",classActivity);
            ((OkyLife) getApplication()).getMasterCaller().postData("SportActivity/getSportActivityById", this, params);
            setContentView(R.layout.show_sport);
            name = (TextView)findViewById(R.id.name_sport);
            date = (TextView)findViewById(R.id.date_sport);
            description = (TextView)findViewById(R.id.description_sport);
            total_calories = (TextView)findViewById(R.id.total_cal);
            total_distance = (TextView)findViewById(R.id.total_dis);
            total_hydration = (TextView)findViewById(R.id.total_hid);
            total_duration = (TextView)findViewById(R.id.total_dur);
            total_rhythm = (TextView)findViewById(R.id.rhythm);
            total_velocity = (TextView)findViewById(R.id.vel);
        }
        else if(classActivity.equals(EAT)){
            Log.v("Es una Comida:",classActivity);
            ((OkyLife) getApplication()).getMasterCaller().postData("EatActivity/getEatActivityById", this, params);
            setContentView(R.layout.show_eat);
            name = (TextView)findViewById(R.id.name_eat);
            date = (TextView)findViewById(R.id.date_eat);
            total_proteins = (TextView)findViewById(R.id.total_proteins);
            total_calories = (TextView)findViewById(R.id.total_calories);
            total_carbohydrates = (TextView)findViewById(R.id.total_carbohydrates);
            total_fat = (TextView)findViewById(R.id.total_fat);
            listIngredients = (ListView)findViewById(R.id.list_ingredients_eat);

        }
        else{
            Log.v("Es visitar un lugar:",classActivity);
            ((OkyLife) getApplication()).getMasterCaller().postData("VisitPlaceActivity/getVisitPlaceActivityById", this, params);
            setContentView(R.layout.show_visit_place);
            name = (TextView)findViewById(R.id.name_place);
            date = (TextView)findViewById(R.id.date_visit_place);
            address = (TextView)findViewById(R.id.address_place);
            description = (TextView)findViewById(R.id.description_visit_place);
            total_calories = (TextView)findViewById(R.id.total_cal);
            total_distance = (TextView)findViewById(R.id.total_dis);
        }

    }

    public void showSport(JSONObject sport){
        try {
            name.setText(sport.getString("name"));
            date.setText(sport.getString("creationDate").split("T")[0]);
            description.setText("\""+sport.getString("description")+"\"");
            total_calories.setText(sport.getString("calories"));
            total_distance.setText(sport.getString("distance"));
            total_hydration.setText(sport.getString("hydration"));
            total_duration.setText(sport.getString("duration"));
            total_rhythm.setText(sport.getString("rhythm"));
            total_velocity.setText(sport.getString("velocity"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void showEat(JSONObject eat){
        try {
            name.setText(eat.getString("name"));
            date.setText(eat.getString("creationDate").split("T")[0]);
            total_proteins.setText(eat.getString("totalProteins"));
            total_calories.setText(eat.getString("totalCalories"));
            total_carbohydrates.setText(eat.getString("totalCarbohydrates"));
            total_fat.setText(eat.getString("totalFat"));
            JSONArray ingredients =eat.getJSONArray("ingredients");
            i_name = new String[ingredients.length()];
            i_description = new String[ingredients.length()];
            i_calories  = new String[ingredients.length()];
            i_carbohydrates  = new String[ingredients.length()];
            i_fat = new String[ingredients.length()];
            i_proteins = new String[ingredients.length()];
            i_number = new String[ingredients.length()];
            i_rationType = new String[ingredients.length()];
            for (int i = 0; i <ingredients.length() ; i++) {
                i_name[i] = ingredients.getJSONObject(i).getString("name");
                i_description[i] = ingredients.getJSONObject(i).getString("description");
                i_calories[i] = ingredients.getJSONObject(i).getString("calories");
                i_carbohydrates[i]= ingredients.getJSONObject(i).getString("carbohydrates");
                i_fat[i]= ingredients.getJSONObject(i).getString("fat");
                i_proteins[i]= ingredients.getJSONObject(i).getString("proteins");
                i_number[i]= ingredients.getJSONObject(i).getString("number");
                i_rationType[i]= ingredients.getJSONObject(i).getString("rationType");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listIngredients.setAdapter(new MyAdapter(this, R.layout.list_ingredients_row, i_name));

    }

    public void showVisitPlace(JSONObject place){
        try {
            name.setText(place.getString("name"));
            date.setText(place.getString("creationDate").split("T")[0]);
            description.setText(place.getString("description"));
            address.setText(place.getString("address"));
            total_distance.setText(place.getString("distance"));
            total_calories.setText(place.getString("calories"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            View row = inflater.inflate(R.layout.list_ingredients_row, parent, false);


            TextView name = (TextView) row.findViewById(R.id.name_ingredient);
            name.setText(i_name[position]);

            TextView description = (TextView) row.findViewById(R.id.description_ingredient);
            description.setText("\""+i_description[position]+"\"");

            TextView num = (TextView) row.findViewById(R.id.number_rates);
            num.setText(i_number[position]);

            TextView rat = (TextView) row.findViewById(R.id.type_rate);
            rat.setText(i_rationType[position]);

            TextView cal = (TextView) row.findViewById(R.id.cal_i);
            cal.setText(i_calories[position]);

            TextView car = (TextView) row.findViewById(R.id.car_i);
            car.setText(i_carbohydrates[position]);

            TextView fat = (TextView) row.findViewById(R.id.fat_i);
            fat.setText(i_fat[position]);

            TextView prot = (TextView) row.findViewById(R.id.prot_i);
            prot.setText(i_proteins[position]);

            return row;
        }
    }


    @Override
    public void processFinish(String result) {
        //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        try{
            if (OkyLife.isJSON(result)) {
                JSONObject activity = new JSONObject(result);

                if(classActivity.equals(SPORT)){
                    Log.v("Deporte: ", result);
                    showSport(activity);

                }
                else if(classActivity.equals(EAT)){
                    Log.v("Comida: ",result);
                    showEat(activity);
                }
                else{
                    Log.v("Visitar un lugar: ",result);
                    showVisitPlace(activity);
                }
            }
        }
        catch(Exception e){
            Log.v("Ninguno de los 3: ",result);
        }

    }

    @Override
    public void onStart() {
        Log.v("location", "started");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.v("location", "stopped");
        super.onStop();
    }


}
