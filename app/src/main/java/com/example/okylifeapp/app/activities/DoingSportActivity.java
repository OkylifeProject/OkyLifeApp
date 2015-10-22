package com.example.okylifeapp.app.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.example.okylifeapp.app.R;
import rest.AsyncResponse;


/**
 * Created by Cristian Parada on 18/10/2015.
 */
public class DoingSportActivity extends Activity implements AsyncResponse {
    String[] strings = {"Correr", "Ciclismo", "Caminar"};

    String[] subs = {"\"Hay gente que corre mas rapido que tu, pero no lo disfruta tanto\"",
                    "\"No se deja de pedalear cuando se envejece. Se envejece cuando se deja de pedalear\"",
                    "\"Cuando salgas a caminar, hazlo despacio, descubriras tantas cosas hermosas.\""};

    int arr_images[] = { R.drawable.run,
            R.drawable.cycling,
            R.drawable.walk};


    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.doing_sports_activity);
        Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
        mySpinner.setAdapter(new MyAdapter(this, R.layout.row, strings));
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

    @Override
    public void onStart() {
        super.onStart();



    }


    @Override
    public void processFinish(String result) {

    }


}
