package com.example.okylifeapp.app.activities;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import aplication.OkyLife;
import com.example.okylifeapp.app.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
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
public class ShowDialogActivity extends Activity implements AsyncResponse {
    private static String SPORT = "okylifeapi.SportActivity";
    private static String EAT = "okylifeapi.EatActivity";
    private static String VISIT_PLACE = "okylifeapi.VisitPlaceActivity";
    private String classActivity;
    private String idActivity;
    private String[] i_name, i_description, i_calories, i_carbohydrates, i_fat, i_proteins, i_number, i_rationType;
    private TextView name, date, description, total_proteins, total_calories, total_carbohydrates, total_fat, total_distance, address,
            total_hydration, total_duration, total_rhythm, total_velocity, title_graph;
    private PieChart pieChart;
    private ListView listIngredients;
    private float infoCal;
    private boolean allowGetCalUser = false;
    private Account okyLifeAccount;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        okyLifeAccount = ((OkyLife) getApplication()).getOkyLifeAccount();
        classActivity = getIntent().getExtras().getString("class");
        idActivity = getIntent().getExtras().getString("id");
        Log.v("id recibido", idActivity);
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id", idActivity));
        if (classActivity.equals(SPORT)) {
            Log.v("Es un Sport:", classActivity);
            ((OkyLife) getApplication()).getMasterCaller().postData("SportActivity/getSportActivityById", this, params);
            setContentView(R.layout.show_sport);
            name = (TextView) findViewById(R.id.name_sport);
            date = (TextView) findViewById(R.id.date_sport);
            description = (TextView) findViewById(R.id.description_sport);
            total_calories = (TextView) findViewById(R.id.total_cal);
            total_distance = (TextView) findViewById(R.id.total_dis);
            total_hydration = (TextView) findViewById(R.id.total_hid);
            total_duration = (TextView) findViewById(R.id.total_dur);
            total_rhythm = (TextView) findViewById(R.id.rhythm);
            total_velocity = (TextView) findViewById(R.id.vel);
        } else if (classActivity.equals(EAT)) {
            Log.v("Es una Comida:", classActivity);
            ((OkyLife) getApplication()).getMasterCaller().postData("EatActivity/getEatActivityById", this, params);
            setContentView(R.layout.show_eat);
            name = (TextView) findViewById(R.id.name_eat);
            date = (TextView) findViewById(R.id.date_eat);
            total_proteins = (TextView) findViewById(R.id.total_proteins);
            total_calories = (TextView) findViewById(R.id.total_calories);
            total_carbohydrates = (TextView) findViewById(R.id.total_carbohydrates);
            total_fat = (TextView) findViewById(R.id.total_fat);
            listIngredients = (ListView) findViewById(R.id.list_ingredients_eat);

        } else {
            Log.v("Es visitar un lugar:", classActivity);
            ((OkyLife) getApplication()).getMasterCaller().postData("VisitPlaceActivity/getVisitPlaceActivityById", this, params);
            setContentView(R.layout.show_visit_place);
            name = (TextView) findViewById(R.id.name_place);
            date = (TextView) findViewById(R.id.date_visit_place);
            address = (TextView) findViewById(R.id.address_place);
            description = (TextView) findViewById(R.id.description_visit_place);
            total_calories = (TextView) findViewById(R.id.total_cal);
            total_distance = (TextView) findViewById(R.id.total_dis);
        }


    }

    public void showSport(JSONObject sport) {
        try {
            description.setText("\"" + sport.getString("description") + "\"");
        } catch (Exception ex) {
            description.setText("");
        }
        try {
            name.setText(sport.getString("name"));
            date.setText(sport.getString("creationDate").split("T")[0]);
            infoCal = Float.parseFloat(sport.getString("calories"));
            total_calories.setText(sport.getString("calories"));
            total_distance.setText(sport.getString("distance"));
            total_hydration.setText(sport.getString("hydration"));
            total_duration.setText(sport.getString("duration"));
            total_rhythm.setText(sport.getString("rhythm"));
            total_velocity.setText(sport.getString("velocity"));
            allowGetCalUser = true;
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", okyLifeAccount.name));
            ((OkyLife) getApplication()).getMasterCaller().postData("User/getUserByEmail", this, params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void showEat(JSONObject eat) {
        try {
            name.setText(eat.getString("name"));
            date.setText(eat.getString("creationDate").split("T")[0]);
            total_proteins.setText(eat.getString("totalProteins"));
            total_calories.setText(eat.getString("totalCalories"));
            total_carbohydrates.setText(eat.getString("totalCarbohydrates"));
            total_fat.setText(eat.getString("totalFat"));
            JSONArray ingredients = eat.getJSONArray("ingredients");
            i_name = new String[ingredients.length()];
            i_description = new String[ingredients.length()];
            i_calories = new String[ingredients.length()];
            i_carbohydrates = new String[ingredients.length()];
            i_fat = new String[ingredients.length()];
            i_proteins = new String[ingredients.length()];
            i_number = new String[ingredients.length()];
            i_rationType = new String[ingredients.length()];
            for (int i = 0; i < ingredients.length(); i++) {
                i_name[i] = ingredients.getJSONObject(i).getString("name");
                i_description[i] = ingredients.getJSONObject(i).getString("description");
                infoCal += Float.parseFloat(ingredients.getJSONObject(i).getString("calories"));
                i_calories[i] = ingredients.getJSONObject(i).getString("calories");
                i_carbohydrates[i] = ingredients.getJSONObject(i).getString("carbohydrates");
                i_fat[i] = ingredients.getJSONObject(i).getString("fat");
                i_proteins[i] = ingredients.getJSONObject(i).getString("proteins");
                i_number[i] = ingredients.getJSONObject(i).getString("number");
                i_rationType[i] = ingredients.getJSONObject(i).getString("rationType");

            }
            listIngredients.setAdapter(new MyAdapter(this, R.layout.list_ingredients_row, i_name));
            allowGetCalUser = true;
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", okyLifeAccount.name));
            ((OkyLife) getApplication()).getMasterCaller().postData("User/getUserByEmail", this, params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void showVisitPlace(JSONObject place) {
        try {
            description.setText("\"" + place.getString("description") + "\"");
        } catch (Exception e) {
            description.setText("");
        }
        try {
            address.setText(place.getString("address"));
        } catch (Exception e) {
            address.setText("");
        }
        try {
            name.setText(place.getString("name"));
            date.setText(place.getString("creationDate").split("T")[0]);
            total_distance.setText(place.getString("distance"));
            infoCal = Float.parseFloat(place.getString("calories"));
            total_calories.setText(place.getString("calories"));
            allowGetCalUser = true;
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", okyLifeAccount.name));
            ((OkyLife) getApplication()).getMasterCaller().postData("User/getUserByEmail", this, params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showGraphic(float calUser) {

        title_graph = (TextView) findViewById(R.id.title_graph);
        pieChart = (PieChart) findViewById(R.id.pieChart);
        /*definimos algunos atributos*/
        pieChart.setHoleRadius(40f);
        pieChart.setDrawYValues(true);
        pieChart.setDrawXValues(true);
        pieChart.setRotationEnabled(true);
        pieChart.animateXY(1500, 1500);

            /*creamos una lista para los valores Y*/
        ArrayList<Entry> valsY = new ArrayList<Entry>();
        float porcent = ((calUser - infoCal) * 100) / calUser;
        if (calUser - infoCal > 0) {
            valsY.add(new Entry(infoCal * 100 / calUser, 1));
            valsY.add(new Entry(porcent, 0));
            title_graph.setText("Has consumido el " + (100 - porcent) + "% de tus calorias meta");
        } else {
            valsY.add(new Entry(calUser * 100 / calUser, 1));
            valsY.add(new Entry(0, 0));
            title_graph.setText("Haz alcanzado la meta con esta actividad");
        }


            /*creamos una lista para los valores X*/
        ArrayList<String> valsX = new ArrayList<String>();
        valsX.add("Gastadas(%)");
        valsX.add("Meta(%)");
            /*creamos una lista de colores*/
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(R.color.red_flat));
        colors.add(getResources().getColor(R.color.blue_flat));

 		/*seteamos los valores de Y y los colores*/
        PieDataSet set1 = new PieDataSet(valsY, "");
        set1.setSliceSpace(3f);
        set1.setColors(colors);

		/*seteamos los valores de X*/
        PieData data = new PieData(valsX, set1);
        pieChart.setData(data);
        pieChart.highlightValues(null);
        pieChart.invalidate();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void processFinish(String result) {
        try {
            if (OkyLife.isJSON(result)) {
                JSONObject activity = new JSONObject(result);

                if (classActivity.equals(SPORT) && !allowGetCalUser) {
                    Log.v("Deporte: ", result);
                    showSport(activity);

                } else if (classActivity.equals(EAT) && !allowGetCalUser) {
                    Log.v("Comida: ", result);
                    showEat(activity);
                } else if (classActivity.equals(VISIT_PLACE) && !allowGetCalUser) {
                    Log.v("Visitar un lugar: ", result);
                    showVisitPlace(activity);
                } else if (allowGetCalUser) {
                    Log.v("Calorias Usuario:", result);
                    showGraphic(Float.parseFloat(activity.getString("calories")));
                }
            }
        } catch (Exception e) {
            Log.v("Ninguno de los 3: ", result);
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
            description.setText("\"" + i_description[position] + "\"");

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


}
