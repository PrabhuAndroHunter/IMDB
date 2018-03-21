package com.imdb.network;

import android.util.Log;


import com.imdb.activity.HomeScreenActivity;
import com.imdb.model.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prabhu on 13/03/2018.
 */

public class ResponseParser {
    private final static String TAG = ResponseParser.class.toString();

    // This method is used for parse stations data
    protected static void parseMovieInfo(List <JSONObject> response, ResponseListener listener) {
        List <Movie> moviesList = new ArrayList <Movie>();
        try {
            for (JSONObject resp : response) {
                JSONArray arr = resp.getJSONArray("results");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jobj = arr.getJSONObject(i);
                    Movie movie = new Movie(jobj.getInt("id"), jobj.getInt("vote_count"), jobj.getString("name"));
                    moviesList.add(movie);
                }
            }
            listener.searchDone(moviesList, "movie_info");
            Log.d(TAG, "Total parsed : " + moviesList.size());
        } catch (Exception e) {
            Log.e(TAG, "parseWeatherInfo: " + e.getMessage());
            listener.searchFail(e.getMessage(), "movie_info");
        }
    }

    protected static void parseNowPlayingMovie(JSONObject resp, ResponseListener listener) {
        List <Movie> nowPlayingMoviesList = new ArrayList <Movie>();
        try {
            JSONArray jsonArray = resp.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                String title = jsonArray.getJSONObject(i).getString("title");
                String posterPath = jsonArray.getJSONObject(i).getString("poster_path");
                String description = jsonArray.getJSONObject(i).getString("overview");
                String releaseDate = jsonArray.getJSONObject(i).getString("release_date");
                String language = jsonArray.getJSONObject(i).getString("original_language");
                String movieId = jsonArray.getJSONObject(i).getString("id");
                String rate = jsonArray.getJSONObject(i).getString("vote_average");
                String banner = jsonArray.getJSONObject(i).getString("backdrop_path");
                // Log.i("Async Task", posterPath);
                Movie m = new Movie(title.trim(), movieId.trim(), releaseDate.trim(), language.trim(), "http://image.tmdb.org/t/p/w500" + posterPath, description.trim(), rate.trim(), "http://image.tmdb.org/t/p/w500" + banner);
                nowPlayingMoviesList.add(m);
            }
            listener.searchDone(nowPlayingMoviesList, HomeScreenActivity.NOW_PLAYING);
            Log.d(TAG, "Total parsed : " + nowPlayingMoviesList.size());
        } catch (Exception e) {
            Log.e(TAG, "parsenowPlayingInfo: " + e.getMessage());
            listener.searchFail(e.getMessage(), HomeScreenActivity.NOW_PLAYING);
        }
    }

    protected static void parseTopRatedMovie(JSONObject resp, ResponseListener listener) {
        List <Movie> topRatedMoviesList = new ArrayList <Movie>();
        try {
            JSONArray jsonArray = resp.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                String title = jsonArray.getJSONObject(i).getString("original_title");
                String posterPath = jsonArray.getJSONObject(i).getString("poster_path");
                String description = jsonArray.getJSONObject(i).getString("overview");
                String releaseDate = jsonArray.getJSONObject(i).getString("release_date");
                String language = jsonArray.getJSONObject(i).getString("original_language");
                String movieId = jsonArray.getJSONObject(i).getString("id");
                String rate = jsonArray.getJSONObject(i).getString("vote_average");
                String banner = jsonArray.getJSONObject(i).getString("backdrop_path");
                // Log.i("Async Task", posterPath);
                Movie m = new Movie(title.trim(), movieId.trim(), releaseDate.trim(), language.trim(), "http://image.tmdb.org/t/p/w500" + posterPath, description.trim(), rate.trim(), "http://image.tmdb.org/t/p/w500" + banner);
                topRatedMoviesList.add(m);
            }
            listener.searchDone(topRatedMoviesList, HomeScreenActivity.TOP_RATED);
            Log.d(TAG, "Total parsed : " + topRatedMoviesList.size());
        } catch (Exception e) {
            Log.e(TAG, "parseTopRatedMovieInfo: " + e.getMessage());
            listener.searchFail(e.getMessage(), HomeScreenActivity.TOP_RATED);
        }
    }

    protected static void parseUpcomingMovie(JSONObject resp, ResponseListener listener) {
        List <Movie> upcomingMoviesList = new ArrayList <Movie>();
        try {
            JSONArray jsonArray = resp.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                String title = jsonArray.getJSONObject(i).getString("original_title");
                String posterPath = jsonArray.getJSONObject(i).getString("poster_path");
                String description = jsonArray.getJSONObject(i).getString("overview");
                String releaseDate = jsonArray.getJSONObject(i).getString("release_date");
                String language = jsonArray.getJSONObject(i).getString("original_language");
                String movieId = jsonArray.getJSONObject(i).getString("id");
                String rate = jsonArray.getJSONObject(i).getString("vote_average");
                String banner = jsonArray.getJSONObject(i).getString("backdrop_path");
                Movie m = new Movie(title.trim(), movieId.trim(), releaseDate.trim(), language.trim(), "http://image.tmdb.org/t/p/w500" + posterPath, description.trim(), rate.trim(), "http://image.tmdb.org/t/p/w500" + banner);
                upcomingMoviesList.add(m);
            }
            listener.searchDone(upcomingMoviesList, HomeScreenActivity.UPCOMING);
            Log.d(TAG, "Total parsed : " + upcomingMoviesList.size());
        } catch (Exception e) {
            Log.e(TAG, "parseUpcomingMovieInfo: " + e.getMessage());
            listener.searchFail(e.getMessage(), HomeScreenActivity.UPCOMING);
        }
    }

    protected static void parsePopularMovie(JSONObject resp, ResponseListener listener) {
        List <Movie> popularMoviesList = new ArrayList <Movie>();
        try {
            JSONArray jsonArray = resp.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                String title = jsonArray.getJSONObject(i).getString("original_title");
                String posterPath = jsonArray.getJSONObject(i).getString("poster_path");
                String description = jsonArray.getJSONObject(i).getString("overview");
                String releaseDate = jsonArray.getJSONObject(i).getString("release_date");
                String language = jsonArray.getJSONObject(i).getString("original_language");
                String movieId = jsonArray.getJSONObject(i).getString("id");
                String rate = jsonArray.getJSONObject(i).getString("vote_average");
                String banner = jsonArray.getJSONObject(i).getString("backdrop_path");
                Movie m = new Movie(title.trim(), movieId.trim(), releaseDate.trim(), language.trim(), "http://image.tmdb.org/t/p/w500" + posterPath, description.trim(), rate.trim(), "http://image.tmdb.org/t/p/w500" + banner);
                popularMoviesList.add(m);
            }
            listener.searchDone(popularMoviesList, HomeScreenActivity.POPULAR);
            Log.d(TAG, "Total parsed : " + popularMoviesList.size());
        } catch (Exception e) {
            Log.e(TAG, "parse Popular MovieInfo: " + e.getMessage());
            listener.searchFail(e.getMessage(), HomeScreenActivity.POPULAR);
        }
    }
}
