package com.example.okylifeapp.app.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
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
    Button btnPlay;
    Button btnStop;
    Chronometer chronometer;
    String currenTime="";
    long elapsedTime = 0;
    boolean isPlaying = false,init=true;
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
        btnPlay = (Button)findViewById(R.id.btnPlay);
        btnStop = (Button)findViewById(R.id.btnStop);
        chronometer = (Chronometer)findViewById(R.id.chronometer);

        btnStop.setEnabled(false);

        mySpinner.setAdapter(new MyAdapter(this, R.layout.row, strings));
        btnPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                btnStop.setEnabled(true);
                isPlaying = !isPlaying;
                if (isPlaying) {
                    if(init==true){
                        chronometer.setBase(SystemClock.elapsedRealtime());
                    }
                    chronometer.start();
                    pause();
                } else {
                    init = false;
                    chronometer.stop();
                    chronometer.setText(currenTime);
                    play();
                }
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                chronometer.stop();
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.setText("00:00");
                isPlaying = false;
                init = true;
                btnPlay.setEnabled(true);
                play();
                btnStop.setEnabled(false);
            }
        });
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {

                if (isPlaying == true) {
                    if (init == true) {
                        long minutes = ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000) / 60;
                        String min = "" + minutes;
                        long seconds = ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000) % 60;
                        String seg = "" + seconds;
                        if(minutes<10){
                            min="0"+min;
                        }
                        if(seconds<10){
                            seg="0"+seg;
                        }
                        currenTime = min + ":" + seg;
                        chronometer.setText(currenTime);
                        elapsedTime = SystemClock.elapsedRealtime();
                    } else {
                        long minutes = ((elapsedTime - chronometer.getBase()) / 1000) / 60;
                        String min = ""+minutes;
                        long seconds = ((elapsedTime - chronometer.getBase()) / 1000) % 60;
                        String seg = ""+seconds;
                        if(minutes<10){
                            min="0"+min;
                        }
                        if(seconds<10){
                            seg="0"+seg;
                        }

                        currenTime = min + ":" + seg;
                        chronometer.setText(currenTime);
                        elapsedTime = elapsedTime + 1000;
                    }


                } else if (isPlaying == false&&init==false) {
                    long minutes = ((elapsedTime - chronometer.getBase()) / 1000) / 60;
                    String min = ""+minutes;
                    long seconds = ((elapsedTime - chronometer.getBase()) / 1000) % 60;
                    String seg = "" + seconds;
                    if(minutes<10){
                        min="0"+min;
                    }
                    if(seconds<10){
                        seg="0"+seg;
                    }
                    currenTime = min + ":" + seg;
                    chronometer.setText(currenTime);
                    elapsedTime = elapsedTime + 1000;
                }
            }
        });
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

    public  void pause (){
        btnPlay.setBackgroundResource(R.drawable.pause);
    }
    public  void play (){
        btnPlay.setBackgroundResource(R.drawable.play);
    }

}
