package com.example.okylifeapp.app.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import aplication.OkyLife;
import com.example.okylifeapp.app.R;
import rest.AsyncResponse;


/**
 * Created by Cristian Parada on 18/10/2015.
 */

public class ShowDialogNote extends DialogFragment implements AsyncResponse{

    AlertPositiveExitListener alertPositiveExitListener;

    DialogInterface.OnClickListener exitListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            alertPositiveExitListener.onPositiveExitClick(true);
        }
    };
    private String content,name;
    private String [] date;
    private TextView namePerson,contentNote,dateNote;
    private Context context;

    public ShowDialogNote(Context context,String name,String content, String date) {
        this.name = name;
        this.context = context;
        this.content = content;
        this.date = date.split("T");
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            alertPositiveExitListener = (AlertPositiveExitListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement AlertPositiveDeleteListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //setContentView(R.layout.view_history_activities_activity);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = (View) LayoutInflater.from(context).inflate(R.layout.show_note, null);
        namePerson = (TextView) view.findViewById(R.id.name_person);
        contentNote = (TextView) view.findViewById(R.id.content_note);
        dateNote = (TextView) view.findViewById(R.id.date_note);
        namePerson.setText(name);
        dateNote.setText(date[0]);
        contentNote.setText("\""+content+"\"");
        builder.setView(view);

        /** Setting a positive button and its listener **/
        builder.setPositiveButton("OK", exitListener);

        /** Creating the alert dialog window using the builder class */
        AlertDialog alertDialog = builder.create();

        /** Return the alert dialog window */
        return alertDialog;
    }

    @Override
    public void processFinish(String result) {

    }

    interface AlertPositiveExitListener {
        public void onPositiveExitClick(boolean exit);
    }

}
