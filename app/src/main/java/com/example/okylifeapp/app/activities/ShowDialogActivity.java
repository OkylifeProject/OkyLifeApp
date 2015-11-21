package com.example.okylifeapp.app.activities;

import android.accounts.Account;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
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
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        //setContentView(R.layout.view_history_activities_activity);
        classActivity = getIntent().getExtras().getString("class");
        idActivity = getIntent().getExtras().getString("id");
        if(classActivity.equals(SPORT)){
            Log.v("Es un Sport:",classActivity);
            //Hacer llamado a api para traer actividad por ID
        }
        else if(classActivity.equals(EAT)){
            Log.v("Es una Comida:",classActivity);
            //Hacer llamado a api para traer actividad por ID
        }
        else{
            Log.v("Es visitar un lugar:",classActivity);
            //Hacer llamado a api para traer actividad por ID
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void processFinish(String result) {
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        if (OkyLife.isJSON(result)) {
            if(classActivity.equals(SPORT)){
                //Hacer tratamiento de Objeto que se retorna
            }
            else if(classActivity.equals(EAT)){
                //Hacer tratamiento de Objeto que se retorna
            }
            else{
                //Hacer tratamiento de Objeto que se retorna
            }
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
