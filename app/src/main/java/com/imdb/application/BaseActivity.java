package com.imdb.application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.imdb.network.NetworkStateListener;

/**
 * Created by prabhu on 31/3/18.
 */

public class BaseActivity extends AppCompatActivity {
    private final String TAG = BaseActivity.class.toString();
    private NetworkStateListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Log.d(TAG, "onCreate: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        IntentFilter networkstatusFilter = new IntentFilter(intentFilter);
        registerReceiver(netWorkStatusReciever, networkstatusFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
        unregisterReceiver(netWorkStatusReciever);
    }

    BroadcastReceiver netWorkStatusReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            Log.d(TAG, "onReceive: " + isConnected);
            if (listener != null) {
                if (isConnected) {
                    listener.connected();
                    Application.isNetWorkConnected = true;
                } else {
                    listener.notConnected();
                    Application.isNetWorkConnected = false;
                }
            }
        }
    };

    public void registerNetworkReceiver(NetworkStateListener listener) {
        this.listener = listener;
    }

    @Override
    public void startActivity(Intent intent) {
        if (Application.isNetWorkConnected)
            super.startActivity(intent, null);
        else
            showNoNetworkToast();
    }

    public void showNoNetworkToast() {
        Toast.makeText(this, "Please Check Your Internet Connction!", Toast.LENGTH_SHORT).show();
    }
}
