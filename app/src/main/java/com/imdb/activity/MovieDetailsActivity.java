package com.imdb.activity;

import android.media.Image;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.imdb.R;
import com.imdb.adapter.MovieArtistDetailsAdapter;
import com.imdb.adapter.RecyclerViewAdapter;
import com.imdb.application.Application;
import com.imdb.application.BaseActivity;
import com.imdb.model.Cast;
import com.imdb.model.Crew;
import com.imdb.model.Movie;
import com.imdb.model.MovieDetails;
import com.imdb.network.RequestHelper;
import com.imdb.network.ResponseListener;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailsActivity extends BaseActivity implements ResponseListener {
    private final String TAG = MovieDetailsActivity.class.toString();
    //    CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ImageView mMoviePosterImv, mBackBtn;
    private TextView mMovieTitleTv, mVoteCountTv, mRatings, mReleaseDateTv, mLanguageTv, mWebsiteTv, mBudgetTv, mRevenueTv, mGenres, mDetailsTv;
    private AppBarLayout appBarLayout;
    private RecyclerView mCastRv, mCrewRv;
    private MovieArtistDetailsAdapter movieArtistDetailsCastAdapter, movieArtistDetailsCrewAdapter;

    public static final String CAST = "cast";
    public static final String CREW = "crew";
    public static final String MOVIE_DETAILS = "movie_details";

    private Movie currentMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
//        mToolbar = (Toolbar) findViewById(R.id.toolbar_movie_details);
//        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_movie_details);
        mMoviePosterImv = (ImageView) findViewById(R.id.image_view_movie_poster);
        mMovieTitleTv = (TextView) findViewById(R.id.text_view_title);
        mVoteCountTv = (TextView) findViewById(R.id.text_view_vote_count);
        mRatings = (TextView) findViewById(R.id.text_view_ratings);
        mReleaseDateTv = (TextView) findViewById(R.id.text_view_release_date);
        mBackBtn = (ImageView) findViewById(R.id.button_back);
//        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        mCastRv = (RecyclerView) findViewById(R.id.recycler_view_cast);
        mCrewRv = (RecyclerView) findViewById(R.id.recycler_view_crew);
        mCastRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mCrewRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        mLanguageTv = (TextView) findViewById(R.id.text_view_movie_language);
        mWebsiteTv = (TextView) findViewById(R.id.text_view_movie_website);
        mBudgetTv = (TextView) findViewById(R.id.text_view_movie_budget);
        mRevenueTv = (TextView) findViewById(R.id.text_view_movie_revenue);
        mGenres = (TextView) findViewById(R.id.text_view_movie_genres);
        mDetailsTv = (TextView) findViewById(R.id.text_view_movie_details);

        currentMovie = Application.getCurrentPlayingMovie();
/*
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
*/

        loadMovieDetails();

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_change, R.anim.slide_out_right);
    }


    private void loadMovieDetails() {
        RequestHelper.getMovieDetails(currentMovie.getMovieId(), this);
        RequestHelper.getMovieArtistDetails(currentMovie.getMovieId(), this);
        Glide.with(this)
                .load(currentMovie.getBanner())
                .crossFade()
                .into(mMoviePosterImv);
        mMovieTitleTv.setText(currentMovie.getTitle());
        mVoteCountTv.setText("" + currentMovie.getVoteCount());
        mRatings.setText(currentMovie.getRating() + "/10");
        mReleaseDateTv.setText(currentMovie.getReleaseDate());
        movieArtistDetailsCastAdapter = new MovieArtistDetailsAdapter(this, MovieArtistDetailsAdapter.CAST);
        movieArtistDetailsCrewAdapter = new MovieArtistDetailsAdapter(this, MovieArtistDetailsAdapter.CREW);
        mCastRv.setAdapter(movieArtistDetailsCastAdapter);
        mCrewRv.setAdapter(movieArtistDetailsCrewAdapter);
    }

    @Override
    public void searchDone(Object object, String tag) {
        switch (tag) {
            case MOVIE_DETAILS:
                MovieDetails movieDetails = (MovieDetails) object;
                List <String> genresList = movieDetails.getGenresList();
                if (movieDetails.getLanguage().equalsIgnoreCase("null") || movieDetails.getLanguage().equalsIgnoreCase(""))
                    mLanguageTv.setText("Language Not Available");
                else
                    mLanguageTv.setText(movieDetails.getLanguage());
                if (movieDetails.getWebsite().equalsIgnoreCase("null") || (movieDetails.getWebsite().trim()).equalsIgnoreCase(""))
                    mWebsiteTv.setText("Website Not Available");
                else
                    mWebsiteTv.setText(movieDetails.getWebsite());

                Log.d(TAG, "searchDone: website " + movieDetails.getWebsite());
                if (movieDetails.getBudget() == 0)
                    mBudgetTv.setText("Budget Not Available");
                else
                    mBudgetTv.setText("" + movieDetails.getBudget() + " " + getString(R.string.Rs_words));
                if (movieDetails.getRevenue() == 0)
                    mRevenueTv.setText("Revenue Not Available");
                else
                    mRevenueTv.setText("" + movieDetails.getRevenue() + " " + getString(R.string.Rs_words));
                String genres = "";
                if (genresList.size() > 0) {
                    for (String gen : genresList) {
                        genres = genres + " | " + gen;
                    }
                    genres.trim();
                    String genersArray[] = genres.split("|", 4);
                    mGenres.setText(genersArray[3]);
                } else {
                    mGenres.setText("");
                }

                mDetailsTv.setText(currentMovie.getDescription());
                break;
            case CAST:
                List <Cast> castList = (ArrayList <Cast>) object;
                for (Cast cast : castList) {
                    Log.d(TAG, "cast details  charecter " + cast.getCharacter());
                }
                movieArtistDetailsCastAdapter.refreshUI(castList);
                break;
            case CREW:
                List <Crew> crewList = (ArrayList <Crew>) object;
                movieArtistDetailsCrewAdapter.refreshUI(crewList);
                break;
        }
    }

    @Override
    public void searchFail(String error, String tag) {

    }
}
