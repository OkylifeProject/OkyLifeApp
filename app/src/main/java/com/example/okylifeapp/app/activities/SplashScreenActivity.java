package com.example.okylifeapp.app.activities;

/**
 * Created by Cristian Parada on 15/10/2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.ImageView;
import com.example.okylifeapp.app.R;

public class SplashScreenActivity extends Activity {

    /**
     * The thread to process splash screen events
     */
    private Thread mSplashThread;
    private ImageView splashImageView;
    private AnimationDrawable frameAnimation;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Splash screen view
        setContentView(R.layout.splash_screen);
        splashImageView = (ImageView) findViewById(R.id.SplashImageView);
        splashImageView.setBackgroundResource(R.drawable.flag);
        frameAnimation = (AnimationDrawable) splashImageView.getBackground();
        splashImageView.post(new Runnable() {
            @Override
            public void run() {
                frameAnimation.start();
            }
        });


        final SplashScreenActivity sPlashScreen = this;

        // The thread to wait for splash screen events
        mSplashThread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        // Wait given period of time or exit on touch
                        wait(6500);
                    }
                } catch (InterruptedException ex) {
                }

                // RunSportActivity next activity
                Intent intent = new Intent();
                intent.setClass(sPlashScreen, OkyLifeStartActivity.class);
                startActivity(intent);
                finish();
            }
        };

        mSplashThread.start();
    }

    /**
     * Processes splash screen touch events
     */
    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        if (evt.getAction() == MotionEvent.ACTION_DOWN) {
            synchronized (mSplashThread) {
                mSplashThread.notifyAll();
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        frameAnimation.stop();
        for (int i = 0; i < frameAnimation.getNumberOfFrames(); ++i) {
            Drawable frame = frameAnimation.getFrame(i);
            if (frame instanceof BitmapDrawable) {
                ((BitmapDrawable) frame).getBitmap().recycle();
            }
            frame.setCallback(null);
        }
        frameAnimation.setCallback(null);
    }
}
