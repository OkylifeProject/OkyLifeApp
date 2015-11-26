package com.example.okylifeapp.app.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import aplication.OkyLife;
import com.example.okylifeapp.app.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.utils.ColorTemplate;
import dialogs.LogoutDialog;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import rest.AsyncResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Cristian Parada on 18/10/2015.
 */
public class StaticsActivity extends Activity implements AsyncResponse, LogoutDialog.AlertPositiveLogoutListener {
    private static String SPORT = "okylifeapi.SportActivity";
    private static String EAT = "okylifeapi.EatActivity";
    private static String VISIT_PLACE = "okylifeapi.VisitPlaceActivity";
    private Account okyLifeAccount;
    private String typeClass,id;
    private Float totalProteins,totalCarbohydrates,totalCalories,totalFat;
    boolean startReading = false;
    private int count = 0,count1=0;
    private PieChart pieChart;
    private BarChart barChart;
    private LineChart lineChart;
    private ArrayList<BarEntry> entriesBar = new ArrayList<BarEntry>();
    private ArrayList<Entry> entriesLine = new ArrayList<Entry>();
    private ArrayList<String> labelsBar = new ArrayList<String>();
    private ArrayList<String> labelsLine = new ArrayList<String>();
    private Map<String, Float> date_dis= new HashMap<String, Float>();
    private Map<String, Float> date_cal= new HashMap<String, Float>();
    private List fecha_index = new ArrayList();
    private List fecha_index1 = new ArrayList();
    private TextView title_graph_pie,title_graph_bar,title_graph_line;
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_statistics_activities);
        title_graph_pie =(TextView)findViewById(R.id.title_graph_pie);
        title_graph_pie.setText("Informacion Nutricional Alimentos Consumidos");
        title_graph_bar =(TextView)findViewById(R.id.title_graph_bar);
        title_graph_bar.setText("Distancia Total Recorrida");
        title_graph_line =(TextView)findViewById(R.id.title_graph_bar);
        title_graph_line.setText("Distancia Total Recorrida");
        pieChart = (PieChart) findViewById(R.id.pieChartStatics);
        barChart = (BarChart) findViewById(R.id.barChartStatics);
        lineChart = (LineChart) findViewById(R.id.lineChartStatics);
        okyLifeAccount = ((OkyLife) getApplication()).getOkyLifeAccount();
        getNotesUsers();
    }

    public void getNotesUsers(){
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", okyLifeAccount.name));
        ((OkyLife) getApplication()).getMasterCaller().postData("Activity/getUserActivities", this, params);
    }

    public void publicStaticsActivities(JSONArray arrayActivities){
        startReading= true;

        totalCalories= 0.0f;
        totalCarbohydrates = 0.0f;
        totalFat = 0.0f;
        totalProteins = 0.0f;

        for (int i = 0; i <arrayActivities.length() ; i++) {
            try {

                JSONObject note = (JSONObject) arrayActivities.get(i);
                typeClass =note.getString("class");
                id =note.getString("id");
                ArrayList<NameValuePair> paramsS = new ArrayList<NameValuePair>();
                paramsS.add(new BasicNameValuePair("id", id));
                if (typeClass.equals(EAT)) {
                    Log.v("Es una Comida:", typeClass);
                    ((OkyLife) getApplication()).getMasterCaller().postData("EatActivity/getEatActivityById", this, paramsS);
                }
                else if (typeClass.equals(SPORT)) {
                    Log.v("Es una Deporte:", typeClass);
                    ((OkyLife) getApplication()).getMasterCaller().postData("SportActivity/getSportActivityById", this, paramsS);
                }
                else if (typeClass.equals(VISIT_PLACE)) {
                    Log.v("Es una Lugar:", typeClass);
                    ((OkyLife) getApplication()).getMasterCaller().postData("VisitPlaceActivity/getVisitPlaceActivityById", this, paramsS);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    public void showCircleGraphic(){

        /*definimos algunos atributos*/
        pieChart.setHoleRadius(40f);
        pieChart.setDrawYValues(true);
        pieChart.setDrawXValues(true);
        pieChart.setRotationEnabled(true);
        pieChart.animateXY(1500, 1500);

            /*creamos una lista para los valores Y*/
        ArrayList<Entry> valsY = new ArrayList<Entry>();
        float total = totalCalories+totalCarbohydrates+totalProteins+totalFat;
        valsY.add(new Entry(totalCalories * 100 / total, 0));
        valsY.add(new Entry(totalCarbohydrates * 100/total, 1));
        valsY.add(new Entry(totalProteins*100/total, 2));
        valsY.add(new Entry(totalFat*100/total, 3));

            /*creamos una lista para los valores X*/
        ArrayList<String> valsX = new ArrayList<String>();
        valsX.add("Calorias(%)");
        valsX.add("Carbohidratos(%)");
        valsX.add("Proteinas(%)");
        valsX.add("Grasas(%)");
            /*creamos una lista de colores*/
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(R.color.red_flat));
        colors.add(getResources().getColor(R.color.blue_flat));
        colors.add(getResources().getColor(R.color.orange_flat));
        colors.add(getResources().getColor(R.color.gree_flat));

 		/*seteamos los valores de Y y los colores*/
        PieDataSet set1 = new PieDataSet(valsY, "");
        set1.setSliceSpace(3f);
        set1.setColors(colors);
        /*seteamos los valores de X*/
        PieData data = new PieData(valsX, set1);
        pieChart.setData(data);
        pieChart.setDescription("");
        pieChart.highlightValues(null);
        pieChart.setScaleX(0.8f);
        pieChart.setScaleY(0.8f);
        pieChart.invalidate();


    }

    @Override
    public void onStart() {
        super.onStart();

    }


    @Override
    public void processFinish(String result) {
        if (OkyLife.isJSON(result)&&!startReading) {
            try {
                JSONArray arrayNotes = new JSONArray(result);
                publicStaticsActivities(arrayNotes);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(startReading){
            try {
                JSONObject eat = new JSONObject(result);
                Log.v("Es Comida?: ", result);
                totalProteins = totalProteins + Float.parseFloat(eat.getString("totalProteins"));
                totalCalories = totalCalories + Float.parseFloat(eat.getString("totalCalories"));
                String key = eat.getString("creationDate").split("T")[0];
                if(date_cal.get(key)==null){
                    float  value = Float.parseFloat(eat.getString("totalCalories"));
                    date_cal.put(key,value);
                    fecha_index1.add(count1,key);
                    entriesLine.add(new Entry(value,count1++));
                    labelsLine.add(key);
                }
                else{
                    float value = date_cal.get(key);
                    float cal = Float.parseFloat(eat.getString("totalCalories"));
                    value = value + cal;
                    date_cal.put(key,value);
                    entriesLine.set(fecha_index1.indexOf(key),new Entry(value, fecha_index1.indexOf(key)));
                }
                totalCarbohydrates = totalCarbohydrates + Float.parseFloat(eat.getString("totalCarbohydrates"));
                totalFat = totalFat + Float.parseFloat(eat.getString("totalFat"));

                Log.v("Si es comida!: ", result);
                showCircleGraphic();
            } catch (JSONException e) {
                Log.v(" entonces Es Deporte?: ", result);
                try {
                    JSONObject sport = new JSONObject(result);
                    String key = sport.getString("creationDate").split("T")[0];
                    if(date_dis.get(key)==null){
                        float  value = Float.parseFloat(sport.getString("distance"));
                        fecha_index.add(count,key);
                        date_dis.put(key, value);
                        entriesBar.add(new BarEntry(value, count++));
                        labelsBar.add(key);
                    }else{
                        float value = date_dis.get(key);
                        float distance = Float.parseFloat(sport.getString("distance"));
                        value = value + distance;
                        date_dis.put(key, value);
                        entriesBar.set(fecha_index.indexOf(key), new BarEntry(value, fecha_index.indexOf(key)));
                    }
                    if(date_cal.get(key)==null){
                        float  value = Float.parseFloat(sport.getString("calories"));
                        date_cal.put(key, value);
                        fecha_index1.add(count1, key);
                        entriesLine.add(new Entry(value,count1++));
                        labelsLine.add(key);
                    }
                    else{
                        float value = date_cal.get(key);
                        float cal = Float.parseFloat(sport.getString("calories"));
                        value = value + cal;
                        date_cal.put(key,value);
                        entriesLine.set(fecha_index1.indexOf(key),new Entry(value, fecha_index1.indexOf(key)));
                    }
                    BarDataSet dataset = new BarDataSet(entriesBar, "Dias");


                    dataset.setColors(ColorTemplate.COLORFUL_COLORS);
                    BarData data = new BarData(labelsBar, dataset);
                    barChart.animateY(2000);
                    barChart.setData(data);
                    barChart.setScaleX(0.8f);
                    barChart.setScaleY(0.8f);
                } catch (JSONException f) {
                    f.printStackTrace();
                }
            }
            LineDataSet dataset = new LineDataSet(entriesLine, "Dias");
            dataset.setColors(ColorTemplate.COLORFUL_COLORS);
            LineData data = new LineData(labelsLine, dataset);
            lineChart.animateY(2000);
            lineChart.setScaleEnabled(true);
            lineChart.setData(data);
            lineChart.setScaleX(0.8f);
            lineChart.setScaleY(0.8f);
        }
        else {
            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        }
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

    @Override
    public void onPositiveLogoutClick(boolean logout) {
        OkyLife.deleteAccount(AccountManager.get(getApplicationContext()), ((OkyLife) getApplication()).getOkyLifeAccount());
        Intent intent = new Intent(this, OkyLifeStartActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
