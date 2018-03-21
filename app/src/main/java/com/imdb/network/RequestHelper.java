package com.imdb.network;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.imdb.activity.HomeScreenActivity;
import com.imdb.application.Application;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prabhu on 13/03/2018.
 */

public class RequestHelper {
    private final static String TAG = RequestHelper.class.toString();

    public static void getMovieInfo(final ResponseListener listner) {
        final List <JSONObject> movieObj = new ArrayList <JSONObject>();
        final String URL = Url.GET_MOVIE;
        JsonObjectRequest movieReq = new JsonObjectRequest(Request.Method.GET,
                URL, null, new Response.Listener <JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                movieObj.add(response);

                // Call ResponseParser to parse the response
                Log.d(TAG, "onResponse: ");
                ResponseParser.parseMovieInfo(movieObj, listner);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: ");
                listner.searchFail(error.getMessage(), "movie_info");
            }
        });
        Application.getInstance().addToRequestQueue(movieReq);
    }

    public static void getNowPlayingMovies(final ResponseListener listner) {
        final String URL = Url.MOVIE_NOW_PLAYING;
        Log.d(TAG, "get Now Playing Movies: " + URL);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,
                URL, null, new Response.Listener <JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                // Call ResponseParser to parse the response
                Log.d(TAG, "onResponse: ");
                ResponseParser.parseNowPlayingMovie(response, listner);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: ");
                listner.searchFail(error.getMessage(), HomeScreenActivity.NOW_PLAYING);
            }
        });
        Application.getInstance().addToRequestQueue(req);
    }

    public static void getTopRatedMovies(final ResponseListener listner) {
        final String URL = Url.MOVIE_TOP_RATED;
        Log.d(TAG, "get Top Rated Movies: " + URL);
        JsonObjectRequest getTopRatedMoviesReq = new JsonObjectRequest(Request.Method.GET,
                URL, null, new Response.Listener <JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                // Call ResponseParser to parse the response
                Log.d(TAG, "onResponse: ");
                ResponseParser.parseTopRatedMovie(response, listner);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: ");
                listner.searchFail(error.getMessage(), HomeScreenActivity.TOP_RATED);
            }
        });
        Application.getInstance().addToRequestQueue(getTopRatedMoviesReq);
    }

    public static void getUpcomingMovies(final ResponseListener listner) {
        final String URL = Url.MOVIE_UPCOMING;
        Log.d(TAG, "get Upcoming Movies: " + URL);
        JsonObjectRequest getUpcomingMoviesReq = new JsonObjectRequest(Request.Method.GET,
                URL, null, new Response.Listener <JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                // Call ResponseParser to parse the response
                Log.d(TAG, "onResponse: ");
                ResponseParser.parseUpcomingMovie(response, listner);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: ");
                listner.searchFail(error.getMessage(), HomeScreenActivity.UPCOMING);
            }
        });
        Application.getInstance().addToRequestQueue(getUpcomingMoviesReq);
    }

    public static void getMostPopularMovies(final ResponseListener listner) {
        final String URL = Url.MOVIE_POPULAR;
        Log.d(TAG, "get popular Movies: " + URL);
        JsonObjectRequest getPopularMoviesReq = new JsonObjectRequest(Request.Method.GET,
                URL, null, new Response.Listener <JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                // Call ResponseParser to parse the response
                Log.d(TAG, "onResponse: ");
                ResponseParser.parsePopularMovie(response, listner);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: ");
                listner.searchFail(error.getMessage(), HomeScreenActivity.POPULAR);
            }
        });
        Application.getInstance().addToRequestQueue(getPopularMoviesReq);
    }
}
