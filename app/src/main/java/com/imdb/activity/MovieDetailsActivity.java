package com.imdb.activity;

import android.media.Image;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.imdb.R;
import com.imdb.application.Application;
import com.imdb.model.Movie;
import com.imdb.model.MovieDetails;
import com.imdb.network.RequestHelper;
import com.imdb.network.ResponseListener;

import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity implements ResponseListener {
    private final String TAG = MovieDetailsActivity.class.toString();
    private Toolbar mToolbar;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    private RecyclerView recyclerView;
    private ImageView mMoviePosterImv;
    private TextView mVoteCountTv, mRatings, mReleaseDateTv, mLanguageTv, mWebsiteTv, mBudgetTv, mRevenueTv, mGenres;
    private AppBarLayout appBarLayout;

    private Movie currentMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_movie_details);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_movie_details);
        mMoviePosterImv = (ImageView) findViewById(R.id.image_view_movie_poster);
        mVoteCountTv = (TextView) findViewById(R.id.text_view_vote_count);
        mRatings = (TextView) findViewById(R.id.text_view_ratings);
        mReleaseDateTv = (TextView) findViewById(R.id.text_view_release_date);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);

        mLanguageTv = (TextView) findViewById(R.id.text_view_movie_language);
        mWebsiteTv = (TextView) findViewById(R.id.text_view_movie_website);
        mBudgetTv = (TextView) findViewById(R.id.text_view_movie_budget);
        mRevenueTv = (TextView) findViewById(R.id.text_view_movie_revenue);
        mGenres = (TextView) findViewById(R.id.text_view_movie_genres);

        currentMovie = Application.getCurrentPlayingMovie();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        loadMovieDetails();
    }

    private void loadMovieDetails() {
        RequestHelper.getMovieDetails(currentMovie.getMovieId(), this);
        RequestHelper.getMovieArtistDetails(currentMovie.getMovieId(), this);
        Glide.with(this)
                .load(currentMovie.getBanner())
                .crossFade()
                .into(mMoviePosterImv);
        mCollapsingToolbarLayout.setTitle(currentMovie.getTitle());
        mVoteCountTv.setText("" + currentMovie.getVoteCount());
        mRatings.setText(currentMovie.getRating() + "/10");
        mReleaseDateTv.setText(currentMovie.getReleaseDate());
    }

    @Override
    public void searchDone(Object object, String tag) {
        MovieDetails movieDetails = (MovieDetails) object;
        List <String> genresList = movieDetails.getGenresList();
        mLanguageTv.setText(movieDetails.getLanguage());
        mWebsiteTv.setText(movieDetails.getWebsite());
        mBudgetTv.setText("" + movieDetails.getBudget());
        mRevenueTv.setText("" + movieDetails.getRevenue());
        String genres = "";
        if (genresList.size() > 0 ){
            for (String gen : genresList) {
                genres = genres + " | " + gen;
            }
            genres.trim();
            String genersArray[] = genres.split("|", 4);
            mGenres.setText(genersArray[3]);
        }else {
            mGenres.setText("");
        }

    }

    @Override
    public void searchFail(String error, String tag) {

    }
}
