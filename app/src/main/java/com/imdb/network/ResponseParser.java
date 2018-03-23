package com.imdb.network;

import android.util.Log;


import com.imdb.activity.HomeScreenActivity;
import com.imdb.model.Movie;
import com.imdb.model.MovieDetails;
import com.imdb.model.ProductionCompanies;

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
                int voteCount = jsonArray.getJSONObject(i).getInt("vote_count");
                String banner = jsonArray.getJSONObject(i).getString("backdrop_path");
                // Log.i("Async Task", posterPath);
                Movie m = new Movie(title.trim(), movieId.trim(), description.trim(), language.trim(), "http://image.tmdb.org/t/p/w500" + posterPath, releaseDate.trim(), rate.trim(), voteCount, "http://image.tmdb.org/t/p/w500" + banner);
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
                int voteCount = jsonArray.getJSONObject(i).getInt("vote_count");
                String banner = jsonArray.getJSONObject(i).getString("backdrop_path");
                // Log.i("Async Task", posterPath);
                Movie m = new Movie(title.trim(), movieId.trim(), description.trim(), language.trim(), "http://image.tmdb.org/t/p/w500" + posterPath, releaseDate.trim(), rate.trim(), voteCount, "http://image.tmdb.org/t/p/w500" + banner);
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
                int voteCount = jsonArray.getJSONObject(i).getInt("vote_count");
                String banner = jsonArray.getJSONObject(i).getString("backdrop_path");
                Movie m = new Movie(title.trim(), movieId.trim(), description.trim(), language.trim(), "http://image.tmdb.org/t/p/w500" + posterPath, releaseDate.trim(), rate.trim(), voteCount, "http://image.tmdb.org/t/p/w500" + banner);
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
                int voteCount = jsonArray.getJSONObject(i).getInt("vote_count");
                String banner = jsonArray.getJSONObject(i).getString("backdrop_path");
                Movie m = new Movie(title.trim(), movieId.trim(), description.trim(), language.trim(), "http://image.tmdb.org/t/p/w500" + posterPath, releaseDate.trim(), rate.trim(), voteCount, "http://image.tmdb.org/t/p/w500" + banner);
                popularMoviesList.add(m);
            }
            listener.searchDone(popularMoviesList, HomeScreenActivity.POPULAR);
            Log.d(TAG, "Total parsed : " + popularMoviesList.size());
        } catch (Exception e) {
            Log.e(TAG, "parse Popular MovieInfo: " + e.getMessage());
            listener.searchFail(e.getMessage(), HomeScreenActivity.POPULAR);
        }
    }

    protected static void parseMovieDetails(JSONObject resp, ResponseListener listener) {
        try {
            String backdrop_path = resp.getString("backdrop_path");
            boolean adult = resp.getBoolean("adult");
            int budget = resp.getInt("budget");
            long budgetakfn = resp.getLong("budget");
            Log.d(TAG, "parseMovieDetails: "+budgetakfn);
            Log.d(TAG, "parseMovieDetails: budget "+budget);
            int revenue = resp.getInt("revenue");
            String website = resp.getString("homepage");
            String language = resp.getString("original_language");

            JSONArray genres = resp.getJSONArray("genres");
            List <String> genresList = new ArrayList <String>();
            for (int i = 0; i < genres.length(); i++) {
                genresList.add(genres.getJSONObject(i).getString("name"));
            }

            JSONArray production_companies = resp.getJSONArray("production_companies");
            List <ProductionCompanies> productionCompaniesList = new ArrayList <ProductionCompanies>();
            for (int i = 0; i < production_companies.length(); i++) {
                int id = production_companies.getJSONObject(i).getInt("id");
                String name = production_companies.getJSONObject(i).getString("name");
                String logo = production_companies.getJSONObject(i).getString("logo_path");
                String country = production_companies.getJSONObject(i).getString("origin_country");
                productionCompaniesList.add(new ProductionCompanies(id, name, logo, country));
            }

            MovieDetails movieDetails = new MovieDetails(backdrop_path, adult, budget, revenue, website, language, genresList, productionCompaniesList);
            listener.searchDone(movieDetails, HomeScreenActivity.NON);
        } catch (Exception e) {
            Log.e(TAG, "parse Popular MovieInfo: " + e.getMessage());
            listener.searchFail(e.getMessage(), HomeScreenActivity.NON);
        }
    }
}
