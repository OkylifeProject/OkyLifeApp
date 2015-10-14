package com.example.okylifeapp.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import aplication.OkyLife;
import com.example.okylifeapp.app.R;
import rest.AsyncResponse;

/**
 * Created by mordreth on 10/14/15.
 */
public class ProfileSettingsActivity extends Activity implements AsyncResponse {
    OkyLife app;
    private static final int SELECT_PICTURE = 1;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.edit_profile);
    }

    public void selectImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Selecciona Imagen"), SELECT_PICTURE);
    }

    @Override
    public void processFinish(String result) {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Bitmap yourSelectedImage = null;
                Uri selectedImageStream = data.getData();
                yourSelectedImage = app.decodeSampledBitmapFromUri(selectedImageStream, getContentResolver(), 200, 200);

                ImageButton photoSelectorButton = (ImageButton) findViewById(R.id.photoSelectorButton);
                photoSelectorButton.setImageBitmap(yourSelectedImage);
            }
        }
    }
}
