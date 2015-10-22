package com.example.okylifeapp.app.activities;

import android.widget.ImageView;

/**
 * Created by Cristian Parada on 19/10/2015.
 */
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.okylifeapp.app.R;

public class TypeActivity extends ArrayAdapter<String>{

    private final Activity context;
    private final String[] listTypeActivity;
    private final Integer[] imageId;
    public TypeActivity(Activity context,
                      String[] listTypeActivity, Integer[] imageId) {
        super(context, R.layout.list_single, listTypeActivity);
        this.context = context;
        this.listTypeActivity = listTypeActivity;
        this.imageId = imageId;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        txtTitle.setText(listTypeActivity[position]);

        imageView.setImageResource(imageId[position]);
        return rowView;
    }
}