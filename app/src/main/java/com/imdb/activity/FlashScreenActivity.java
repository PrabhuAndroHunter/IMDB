package com.imdb.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.imdb.R;

public class FlashScreenActivity extends AppCompatActivity {
    private final String TAG = FlashScreenActivity.class.toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init layout
        setContentView(R.layout.activity_flash_screen);
    }

    @Override
    protected void onStart() {
        super.onStart();
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
