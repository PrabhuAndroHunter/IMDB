package com.imdb.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;

import com.imdb.R;
import com.imdb.application.BaseActivity;

/*
*  This is the Launch screen of the Application.
*
* */

public class FlashScreenActivity extends BaseActivity {
    private final String TAG = FlashScreenActivity.class.toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init layout
        setContentView(R.layout.activity_flash_screen);
        Log.d(TAG, "onCreate: ");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        // start wait timer for 2 sec
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // start HomeScreenActivity
                startActivity(new Intent(FlashScreenActivity.this, HomeScreenActivity.class));
                finish();
            }
        }, 2000);
    }
}
