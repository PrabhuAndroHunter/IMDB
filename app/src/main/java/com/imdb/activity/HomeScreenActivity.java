package com.imdb.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.imdb.R;
import com.imdb.adapter.TopTenMoviePosterAdapter;
import com.imdb.adapter.RecyclerViewAdapter;
import com.imdb.model.Movie;
import com.imdb.network.RequestHelper;
import com.imdb.network.ResponseListener;

import java.util.ArrayList;
import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

public class HomeScreenActivity extends AppCompatActivity implements ResponseListener {
    private final String TAG = HomeScreenActivity.class.toString();
    private RecyclerView mNowPlayingRv, mTopRatedRv, mUpcomingRv, mPopularRv;
    private TextView mPageIndexTv;
    private TopTenMoviePosterAdapter topTenPosterAdapter;
    private RecyclerViewAdapter topRatedMovieAdapter, nowPlayingMovieAdapter, upcomingMovieAdapter, popularMovieAdapter;
    List <Movie> topRatedMovieList, nowPlayingMovieList, upcomingMovieList, popularMovieList, topTenMoviePosters;

    private String[] permissions = {Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET};
    private final int PERMISSION_CODE = 17;
    public static final String NOW_PLAYING = "now_playing";
    public static final String TOP_RATED = "topRated";
    public static final String UPCOMING = "upcoming";
    public static final String POPULAR = "popular";
    private AutoScrollViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init layout
        setContentView(R.layout.activity_home_screen);
        viewPager = (AutoScrollViewPager) findViewById(R.id.view_pager);

        mNowPlayingRv = (RecyclerView) findViewById(R.id.recycler_view_now_playing);
        mTopRatedRv = (RecyclerView) findViewById(R.id.recycler_view_top_rated);
        mUpcomingRv = (RecyclerView) findViewById(R.id.recycler_view_upcoming);
        mPopularRv = (RecyclerView) findViewById(R.id.recycler_view_popular);
        mPageIndexTv = (TextView) findViewById(R.id.text_view_page_index);

        mNowPlayingRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mTopRatedRv.setLayoutManager(new LinearLayoutManager(HomeScreenActivity.this, LinearLayoutManager.HORIZONTAL, false));
        mUpcomingRv.setLayoutManager(new LinearLayoutManager(HomeScreenActivity.this, LinearLayoutManager.HORIZONTAL, false));
        mPopularRv.setLayoutManager(new LinearLayoutManager(HomeScreenActivity.this, LinearLayoutManager.HORIZONTAL, false));

        topTenPosterAdapter = new TopTenMoviePosterAdapter(this);
        viewPager.setAdapter(topTenPosterAdapter);
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());

        viewPager.setInterval(2000);
        viewPager.startAutoScroll();
        viewPager.setCycle(true);
        viewPager.setBorderAnimation(true);
        viewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_TO_PARENT);
        viewPager.setCurrentItem(0, true);
        populatRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // check for permissions
        if (!hasPremissions(this, permissions)) {
            // if still not granted then ask permission
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_CODE);
        }

        RequestHelper.getNowPlayingMovies(this);
        RequestHelper.getTopRatedMovies(this);
        RequestHelper.getUpcomingMovies(this);
        RequestHelper.getMostPopularMovies(this);

    }

    /* This method will
     * @return
     * true --> if the permission is already granted
     * false --> if the permission is not granted yet
     */

    private boolean hasPremissions(Context context, String... permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                    return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // check if the user Accespted the permission or not
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Thank You", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void searchDone(Object object, String tag) {
        switch (tag) {
            case NOW_PLAYING:
                nowPlayingMovieList = (ArrayList <Movie>) object;
                Log.d(TAG, "searchDone: nowPlayingMovieList " + nowPlayingMovieList.size());
                nowPlayingMovieAdapter.refreshUI(nowPlayingMovieList);
                break;
            case TOP_RATED:
                topRatedMovieList = (ArrayList <Movie>) object;
                topTenMoviePosters = new ArrayList <Movie>();
                for (int i = 0; i < 10; i++) {
                    topTenMoviePosters.add(topRatedMovieList.get(i));
                    Log.d(TAG, "searchDone: " + topTenMoviePosters.get(i));
                }
                topTenPosterAdapter.setTopTenPosters(topTenMoviePosters);
                Log.d(TAG, "searchDone: topRatedMovieList " + topRatedMovieList.size());
                topRatedMovieAdapter.refreshUI(topRatedMovieList);
                break;
            case UPCOMING:
                upcomingMovieList = (ArrayList <Movie>) object;
                Log.d(TAG, "searchDone: nowPlayingMovieList " + nowPlayingMovieList.size());
                upcomingMovieAdapter.refreshUI(upcomingMovieList);
                break;
            case POPULAR:
                popularMovieList = (ArrayList <Movie>) object;
                Log.d(TAG, "searchDone: popularMovieList " + popularMovieList.size());
                popularMovieAdapter.refreshUI(popularMovieList);
                break;
        }
    }

    @Override
    public void searchFail(String error, String tag) {
        switch (tag) {
            case NOW_PLAYING:
                Log.d(TAG, "searchFail: nowPlayingMovieList " + error);
                break;
            case TOP_RATED:
                Log.d(TAG, "searchFail: topRatedMovieList " + error);
                break;
        }
        Log.e(TAG, "searchFail: " + error);
    }


    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            mPageIndexTv.setText(new StringBuilder().append((position) % topTenMoviePosters.size() + 1).append("/")
                    .append(topTenMoviePosters.size()));
            Log.d(TAG, "onPageSelected: " + position);
            /*if (position == 9)
                viewPager.setCurrentItem(0);
            viewPager.startAutoScroll();*/
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // stop auto scroll when onPause
        viewPager.stopAutoScroll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // start auto scroll when onResume
        viewPager.startAutoScroll();
    }

    private void populatRecyclerView() {
        topRatedMovieAdapter = new RecyclerViewAdapter(this, RecyclerViewAdapter.TOP_RATED_MOVIE);
        nowPlayingMovieAdapter = new RecyclerViewAdapter(this, RecyclerViewAdapter.NOW_PLAYING);
        upcomingMovieAdapter = new RecyclerViewAdapter(this, RecyclerViewAdapter.UPCOMING_MOVIE);
        popularMovieAdapter = new RecyclerViewAdapter(this, RecyclerViewAdapter.POPULAR_MOVIE);

        mNowPlayingRv.setAdapter(nowPlayingMovieAdapter);
        mTopRatedRv.setAdapter(topRatedMovieAdapter);
        mUpcomingRv.setAdapter(upcomingMovieAdapter);
        mPopularRv.setAdapter(popularMovieAdapter);
    }

    public void onViewAllClick(View view) {
        int viewId = view.getId();
        Intent intent = new Intent(this, ViewAllMovieActivity.class);
        switch (viewId) {
            case R.id.text_view_now_playing_view_all:
                intent.putExtra("TAG", "Now Playing");
                startActivity(intent);
                break;
            case R.id.text_view_top_rating_view_all:
                intent.putExtra("TAG", "Top Rating");
                startActivity(intent);
                break;
            case R.id.text_view_up_coming_view_all:
                intent.putExtra("TAG", "UpComing");
                startActivity(intent);
                break;
            case R.id.text_view_popular_view_all:
                intent.putExtra("TAG", "Most Populer");
                startActivity(intent);
                break;
        }
    }
}
