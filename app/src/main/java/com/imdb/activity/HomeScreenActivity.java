package com.imdb.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.imdb.R;
import com.imdb.adapter.ImagePagerAdapter;
import com.imdb.adapter.RecyclerViewAdapter;
import com.imdb.model.Data_Model;
import com.imdb.model.Movie;
import com.imdb.network.RequestHelper;
import com.imdb.network.ResponseListener;

import java.util.ArrayList;
import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

public class HomeScreenActivity extends AppCompatActivity implements ResponseListener {
    private final String TAG = HomeScreenActivity.class.toString();
    private RecyclerView mNowPlayingRv, mTopRatedRv, mUpcomingRv, mPopularRv;
    private ImagePagerAdapter topFivePosterAdapter;
    private RecyclerViewAdapter topRatedMovieAdapter, nowPlayingMovieAdapter, upcomingMovieAdapter, popularMovieAdapter;
    List <Movie> topRatedMovieList, nowPlayingMovieList, upcomingMovieList, popularMovieList;

    private String[] permissions = {Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET};
    private final int PERMISSION_CODE = 17;
    public static final String NOW_PLAYING = "now_playing";
    public static final String TOP_RATED = "topRated";
    public static final String UPCOMING = "upcoming";
    public static final String POPULAR = "popular";


    public static final String[] TITLES = {"Hood", "Full Sleeve Shirt", "Shirt", "Jean Jacket", "Jacket"};
    public static final Integer[] IMAGES = {R.drawable.one, R.drawable.two, R.drawable.one, R.drawable.two, R.drawable.one,};

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

        mNowPlayingRv.setLayoutManager(new LinearLayoutManager(HomeScreenActivity.this, LinearLayoutManager.HORIZONTAL, false));
        mTopRatedRv.setLayoutManager(new LinearLayoutManager(HomeScreenActivity.this, LinearLayoutManager.HORIZONTAL, false));
        mUpcomingRv.setLayoutManager(new LinearLayoutManager(HomeScreenActivity.this, LinearLayoutManager.HORIZONTAL, false));
        mPopularRv.setLayoutManager(new LinearLayoutManager(HomeScreenActivity.this, LinearLayoutManager.HORIZONTAL, false));

        //        viewPager.setAdapter(new PagerAdapter(this, getImageList()).setInfiniteLoop(true));
        topFivePosterAdapter = new ImagePagerAdapter(this);
        viewPager.setAdapter(topFivePosterAdapter);
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());

        viewPager.setInterval(2000);
        viewPager.startAutoScroll();
        viewPager.setCycle(false);
        viewPager.setBorderAnimation(true);
        viewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_TO_PARENT);
        viewPager.setCurrentItem(0);
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
                List <Movie> topFiveMoviePosters = new ArrayList <Movie>();
                for (int i = 0; i < 5; i++) {
                    topFiveMoviePosters.add(topRatedMovieList.get(i));
                    Log.d(TAG, "searchDone: " + topFiveMoviePosters.get(i));
                }
                topFivePosterAdapter.setTopFivePosters(topFiveMoviePosters);
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
//            indexText.setText(new StringBuilder().append((position) % mResources.length + 1).append("/")
//                    .append(mResources.length));
            Log.d(TAG, "onPageSelected: " + position);
            if (position == 5)
                viewPager.setCurrentItem(0);
            viewPager.startAutoScroll();
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

    // populate the list view by adding data to arraylist
    private void populatRecyclerView() {
        topRatedMovieAdapter = new RecyclerViewAdapter(this, RecyclerViewAdapter.TOP_RATED_MOVIE);
        nowPlayingMovieAdapter = new RecyclerViewAdapter(this, RecyclerViewAdapter.NOW_PLAYING);
        upcomingMovieAdapter = new RecyclerViewAdapter(this, RecyclerViewAdapter.UPCOMING_MOVIE);
        popularMovieAdapter = new RecyclerViewAdapter(this, RecyclerViewAdapter.POPULAR_MOVIE);

        mNowPlayingRv.setAdapter(nowPlayingMovieAdapter);// set adapter on recyclerview
        mTopRatedRv.setAdapter(topRatedMovieAdapter);
        mUpcomingRv.setAdapter(upcomingMovieAdapter);
        mPopularRv.setAdapter(popularMovieAdapter);
    }
}
