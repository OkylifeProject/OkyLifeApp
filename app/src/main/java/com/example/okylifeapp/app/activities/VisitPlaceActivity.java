package com.example.okylifeapp.app.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.okylifeapp.app.R;
import rest.AsyncResponse;


/**
 * Created by Cristian Parada on 18/10/2015.
 */
public class VisitPlaceActivity extends Activity implements AsyncResponse {
    Spinner typePlace;
    String[] strings = {"Parque", "Museo", "Centro Comercial","Hospital","Restaurante"};

    String[] subs = {"","","","",""};

    int arr_images[] = { R.drawable.park,
            R.drawable.museum,
            R.drawable.mall,
            R.drawable.hospital,
            R.drawable.restaurant};
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.visit_place_activity);
        typePlace = (Spinner) findViewById(R.id.spinnerTypePlace);
        typePlace.setAdapter(new MyAdapter(this, R.layout.row, strings));
    }
    public class MyAdapter extends ArrayAdapter<String> {

        public MyAdapter(Context context, int textViewResourceId,String [] objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView,ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater=getLayoutInflater();
            View row=inflater.inflate(R.layout.row, parent, false);
            TextView label=(TextView)row.findViewById(R.id.company);
            label.setText(strings[position]);

            TextView sub=(TextView)row.findViewById(R.id.sub);
            sub.setText(subs[position]);

            ImageView icon=(ImageView)row.findViewById(R.id.image);
            icon.setImageResource(arr_images[position]);

            return row;
        }
    }

    public void renderDialogPlaceCancelConfirmation(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Atencion")
                .setMessage("Esta a punto de borrar el registro de la actividad realizada. \nDesea Continuar?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        renderStartActivity();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    public void renderStartActivity() {
        Intent startActivityIntent = new Intent(this, StartActivity.class);
        finish();
        startActivity(startActivityIntent);

    }
    @Override
    public void onStart() {
        super.onStart();



    }


    @Override
    public void processFinish(String result) {

    }




}
