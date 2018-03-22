package com.imdb.model;

import android.util.Log;

/**
 * Created by prabhu on 13/3/18.
 */

public class Movie {
    private final String TAG = Movie.class.toString();
    int id, vote_count;
    String name;

    private String title;
    private String movieId;
    private String description;
    private String lang;
    private String poster;
    private String releaseDate;
    private String rating;
    private String banner;
    private String trailer;
    private int voteCount;


    public Movie(int id, int vote_count, String name) {
        this.id = id;
        this.vote_count = vote_count;
        this.name = name;
    }

    // This Constructor is used in NowPlaying
    public Movie(String title, String movieId, String description, String lang, String poster, String releaseDate, String rating, int voteCount, String banner) {
        this.title = title;
        this.movieId = movieId;
        this.description = description;
        this.lang = lang;
        this.poster = poster;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.voteCount = voteCount;
        this.banner = banner;
        this.trailer = "http://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=8496be0b2149805afa458ab8ec27560c";
        Log.d(TAG, "Movie: "+trailer);
    }

    public String getTitle() {
        return title;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getDescription() {
        return description;
    }

    public String getLang() {
        return lang;
    }

    public String getPoster() {
        return poster;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getRating() {
        return rating;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public String getBanner() {
        return banner;
    }

    public String getTrailer() {
        return trailer;
    }
    //..................................................


    public int getId() {
        return id;
    }

    public int getVote_count() {
        return vote_count;
    }

    public String getName() {
        return name;
    }
}
