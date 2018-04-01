package com.imdb.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.imdb.R;
import com.imdb.adapter.ViewAllMovieAdapter;
import com.imdb.adapter.RecyclerViewAdapter;
import com.imdb.application.BaseActivity;
import com.imdb.model.Movie;
import com.imdb.network.RequestHelper;
import com.imdb.network.ResponseListener;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ViewAllMovieActivity extends BaseActivity implements ResponseListener {
    private final String TAG = ViewAllMovieActivity.class.toString();
    private RecyclerView mviewAllMovieRv;
    private ViewAllMovieAdapter viewAllMovieAdapter;
    List <Movie> viewAllMovieList;
    private String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_movie);
        tag = getIntent().getStringExtra("TAG");
        Log.d(TAG, "onCreate: tag : " + tag);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(tag + "Movies");
        mviewAllMovieRv = (RecyclerView) findViewById(R.id.view_all_movie);
        mviewAllMovieRv.setLayoutManager(new LinearLayoutManager(this));
        viewAllMovieAdapter = new ViewAllMovieAdapter(this, RecyclerViewAdapter.TOP_RATED_MOVIE);
        mviewAllMovieRv.setAdapter(viewAllMovieAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        switch (tag) {
            case "Now Playing":
                Log.d(TAG, "tag: now playing");
                RequestHelper.getNowPlayingMovies(this);
                break;
            case "Top Rating":
                Log.d(TAG, "tag: top rating");
                RequestHelper.getTopRatedMovies(this);
                break;
            case "UpComing":
                Log.d(TAG, "tag: upcoming");
                RequestHelper.getUpcomingMovies(this);
                break;
            case "Most Populer":
                Log.d(TAG, "tag: most popular");
                RequestHelper.getMostPopularMovies(this);
                break;
        }
    }

    @Override
    public void searchDone(Object object, String tag) {

        viewAllMovieList = (ArrayList <Movie>) object;
        viewAllMovieAdapter.refreshUI(viewAllMovieList);
    }

    @Override
    public void searchFail(String error, String tag) {
        Log.d(TAG, "searchFail: nowPlayingMovieList " + error);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_change, R.anim.slide_out_right);
    }

    public void showLoader(){
        findViewById(R.id.layout_loader_progress).setVisibility(View.VISIBLE);
    }

    public void hideLoader(){
        findViewById(R.id.layout_loader_progress).setVisibility(View.INVISIBLE);
    }

}
