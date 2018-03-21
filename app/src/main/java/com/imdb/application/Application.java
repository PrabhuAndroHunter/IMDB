package com.imdb.application;

/**
 * Created by prabhu on 13/03/2018.
 */
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.imdb.model.Movie;

public class Application extends android.app.Application {

    public static final String TAG = Application.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private static Application mInstance;
    private static Movie currentPlayingMovie;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized Application getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public  void addToRequestQueue(Request req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public  void addToRequestQueue(Request req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static Movie getCurrentPlayingMovie() {
        return currentPlayingMovie;
    }

    public static void setCurrentPlayingMovie(Movie currentPlayingMovie) {
        Application.currentPlayingMovie = currentPlayingMovie;
    }
}