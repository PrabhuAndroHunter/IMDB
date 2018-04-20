package com.imdb.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.imdb.R;
import com.imdb.adapter.RecyclerViewAdapter;
import com.imdb.adapter.RecyclerViewTrailerAdapter;
import com.imdb.application.Application;
import com.imdb.application.BaseActivity;
import com.imdb.model.Movie;
import com.imdb.model.MovieDetails;
import com.imdb.model.Trailer;
import com.imdb.network.RequestHelper;
import com.imdb.network.ResponseListener;
import com.imdb.network.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
*
* This is the Activity where user can watch trailers and movie video clips
*
* */

public class TrailerPlayerActivity extends BaseActivity implements YouTubePlayer.OnInitializedListener, ResponseListener {
    private final String TAG = TrailerPlayerActivity.class.toString();
    private TextView mVoteTv, mRatingTv, mRateThisTv, mShareTv, mWatchLater, mDescriptionTv, mReleaseDate, mMovieStatus;
    private YouTubePlayer player;
    private RecyclerView mTrailerRv;
    RatingBar mRatingBarFirst, mRatingBarSecond;
    Button mSubmitBtn;
    TextView mUserRatingTv;
    Bundle bundle;
    String videoKey = "";
    ArrayList <HashMap <String, String>> videoList;
    private List <Trailer> trailerList = new ArrayList <Trailer>();
    private Movie currentMovie;
    private RecyclerViewTrailerAdapter trailersAdapter;
    private boolean isWatchLater = false;
    private static float ratingOne, ratingTwo;
    private String imdbId, status;
    String guest_session_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init layout
        setContentView(R.layout.activity_trailer_player);
        // init all views
        initViews();
        mTrailerRv.setLayoutManager(new LinearLayoutManager(TrailerPlayerActivity.this, LinearLayoutManager.HORIZONTAL, false));
        bundle = getIntent().getBundleExtra("movie");
        videoList = (ArrayList <HashMap <String, String>>) getIntent().getSerializableExtra("videoIdList");
        final YouTubeInitializationResult result = YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(this);
        if (result != YouTubeInitializationResult.SUCCESS) {
            //If there are any issues we can show an error dialog.
            result.getErrorDialog(this, 0).show();
            this.finish();
        }

        YouTubePlayerSupportFragment frag = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_playerview);
        frag.initialize(Url.YOUTUBE_API_KEY, this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("IMDB Player");

        // Make trailer list
        for (HashMap <String, String> map : videoList) {
            for (Map.Entry <String, String> mapEntry : map.entrySet()) {
                String name = mapEntry.getKey();
                final String vedioLink = mapEntry.getValue();
                Trailer trailer = new Trailer(name, vedioLink);
                trailerList.add(trailer);
            }
        }

        // Get First trailer video key to set Default video
        if (trailerList.size() > 0)
            videoKey = trailerList.get(0).getVedioLink();

        // This will called when User Click on rating button
        mRateThisTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingdialog();
            }
        });

        /*
        * This will called when User Click on Watch button
        * But there is no logic implemented Only Ui changes are done
        * */
        mWatchLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isWatchLater) {
                    isWatchLater = false;
                    mWatchLater.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_watchlist_focus, 0, 0);
                    Toast.makeText(TrailerPlayerActivity.this, "Removed From Watchlist", Toast.LENGTH_SHORT).show();

                } else {
                    isWatchLater = true;
                    mWatchLater.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_watchlist_added, 0, 0);
                    Toast.makeText(TrailerPlayerActivity.this, "Added To Watchlist", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // This will called when User Click on Share button
        mShareTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, currentMovie.getTitle());
                    String movieDetails = currentMovie.getTitle() + "\n";
                    movieDetails = movieDetails + "https://www.imdb.com/title/" + imdbId + "\n";
                    i.putExtra(Intent.EXTRA_TEXT, movieDetails);
                    startActivity(Intent.createChooser(i, "Share via"));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });

        // Load all trailers
        loadTrailers();
    }

    // This method will init all views
    private void initViews() {
        mTrailerRv = (RecyclerView) findViewById(R.id.recycler_view_trailer);
        mVoteTv = (TextView) findViewById(R.id.text_view_vote);
        mRatingTv = (TextView) findViewById(R.id.text_view_votestar);
        mRateThisTv = (TextView) findViewById(R.id.text_view_rateings);
        mShareTv = (TextView) findViewById(R.id.text_view_share);
        mWatchLater = (TextView) findViewById(R.id.text_view_watchlist);
        mDescriptionTv = (TextView) findViewById(R.id.text_view_movie_description);
        mReleaseDate = (TextView) findViewById(R.id.text_view_release_date);
        mMovieStatus = (TextView) findViewById(R.id.text_view_staus);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    // This method will get called when user press backButton
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_change, R.anim.slide_out_right);
    }

    // This callback will get called When YOUTUBE is initialized Successfully
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean b) {
        Log.i("player", "player is playing");
        if (null == player) return;
        if (videoKey.isEmpty()) {
            Toast.makeText(this, "Trailer is not Available yet", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!b) {
            this.player = player;
            player.cueVideo(videoKey);
        }
        player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, "player_error", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Get Current movie(User selected movie)
        currentMovie = Application.getCurrentPlayingMovie();
        Log.d(TAG, "onStart: " + currentMovie.getTitle());
        // Request for movie details
        RequestHelper.getMovieDetails(currentMovie.getMovieId(), this);
        // update UI
        mVoteTv.setText("" + currentMovie.getVoteCount());
        mRatingTv.setText(currentMovie.getRating());
        mReleaseDate.setText(currentMovie.getReleaseDate());
        mDescriptionTv.setText(currentMovie.getDescription());
    }

    // This method will load Trailer data to views
    private void loadTrailers() {
        trailersAdapter = new RecyclerViewTrailerAdapter(this, RecyclerViewAdapter.NOW_PLAYING);
        mTrailerRv.setAdapter(trailersAdapter);
        trailersAdapter.refreshUI(trailerList);
    }

    // This method will update Trailer to main Youtube player view
    public void updateTrailer(String videoKey) {
        this.videoKey = videoKey;
        player.cueVideo(videoKey);
        player.play();
    }

    // This callback will get called when all requested data parsed successfully
    @Override
    public void searchDone(Object object, String tag) {
        MovieDetails movieDetails = (MovieDetails) object;
        imdbId = movieDetails.getImdbId();
        status = movieDetails.getStatus();

        if (status.equalsIgnoreCase("") || status.equalsIgnoreCase("null"))
            mMovieStatus.setText("Movie Status Not Available");
        else
            mMovieStatus.setText(status);

    }

    // This callback will get called when fail to get data from the IMDB server
    @Override
    public void searchFail(String error, String tag) {
    }

    // This method will manage Rating dialogue visibility and user events.
    private void showRatingdialog() {
        final Dialog ratingDialog = new Dialog(this);
        ratingDialog.setContentView(R.layout.item_rating);
        ratingDialog.setTitle("Custom Dialog");
        ratingDialog.setCanceledOnTouchOutside(false);

        mRatingBarFirst = (RatingBar) ratingDialog.findViewById(R.id.ratingBar1);
        mRatingBarSecond = (RatingBar) ratingDialog.findViewById(R.id.ratingBar2);
        mUserRatingTv = (TextView) ratingDialog.findViewById(R.id.textview_user_rating);
        mSubmitBtn = (Button) ratingDialog.findViewById(R.id.button_submit);
        mRatingBarSecond.setEnabled(false);

        // Set onclick listener on First Row Rating bar
        mRatingBarFirst.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                String ratedValue = String.valueOf(ratingBar.getRating());
                float ratingOne = Float.valueOf(ratedValue);
                updateRatingFirst(ratingOne);
                Log.d(TAG, "onRatingChanged: " + ratedValue);
            }
        });

        // Set onclick listener on Second Row Rating bar
        mRatingBarSecond.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                String ratedValue = String.valueOf(ratingBar.getRating());
                float ratingTwo = Float.valueOf(ratedValue);
                updateRatingSecond(ratingTwo);
                Log.d(TAG, "onRatingChanged: " + ratedValue);
            }
        });

        // Set onclick listener on Submit buton
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PostRatingData().execute(String.valueOf(ratingTwo));
                ratingDialog.dismiss();
            }
        });
        ratingDialog.show();
    }

    // This will manage Visibility of first row rating bar
    private void updateRatingFirst(float userRating) {
        ratingOne = userRating;
        mUserRatingTv.setText("" + ratingOne);
        if (ratingOne == 5.0) {
            Log.d(TAG, " enableing Second: ");
            mRatingBarSecond.setEnabled(true);
        } else {
            Log.d(TAG, " desabling second");
            mRatingBarSecond.setEnabled(false);
        }
    }

    // This will manage Visibility of second row rating bar
    private void updateRatingSecond(float userRating) {
        ratingTwo = ratingOne + userRating;
        mUserRatingTv.setText("" + ratingTwo);
        Log.d(TAG, "updateRatingSecond: " + userRating);
        if (userRating == 0.0) {
            Log.d(TAG, " enableing first: ");
            mRatingBarFirst.setEnabled(true);
        } else {
            Log.d(TAG, " Desabling first: ");
            mRatingBarFirst.setEnabled(false);
        }
    }

    // This AsyncTask will manage to Post User Rating
    private class PostRatingData extends AsyncTask <String, Void, String> {

        PostRatingData() {
            new GetSessionId().execute("http://api.themoviedb.org/3/authentication/guest_session/new?api_key=8496be0b2149805afa458ab8ec27560c");

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {

                URL url = new URL("https://api.themoviedb.org/3/movie/" + bundle.getString("movieId") + "/rating?api_key=8496be0b2149805afa458ab8ec27560c&guest_session_id=" + guest_session_id); // here is your URL path
                Log.i("url", url.toString());

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("value", strings[0]);

                Log.e("params", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");

                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == 201) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("Fail");
                }
            } catch (Exception e) {
                return new String("Fail");
            }

        }

        @Override
        protected void onPostExecute(String result) {

            Log.d(TAG, "onPostExecute: " + result);
            if (result.equalsIgnoreCase("Fail")) {
                Toast.makeText(getApplicationContext(), result,
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Submitted",
                        Toast.LENGTH_LONG).show();
            }
        }

    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator <String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }

    // This AsyncTask will manage to get guest session ID to post User ratings
    private class GetSessionId extends AsyncTask <String, Void, String> {
        String error = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            BufferedReader br = null;
            URL url = null;
            String content = "";
            try {
                url = new URL(strings[0]);
                Log.i("Session url", url.toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int responseCode = connection.getResponseCode();
                Log.d("Status", responseCode + "");
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                content = sb.toString();
            } catch (MalformedURLException e) {
                error = e.getMessage();
                e.printStackTrace();
            } catch (IOException e) {
                error = e.getMessage();
                e.printStackTrace();
            } finally {
                try {
                    br.close();
                } catch (IOException e) {
                    error = e.getMessage();
                    e.printStackTrace();
                }
            }
            Log.i("Content:", content);
            return content;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (error != null) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONObject respone = new JSONObject(result);
                    String session_id = respone.getString("guest_session_id");
                    guest_session_id = session_id;

                } catch (JSONException ex) {
                    Log.e("Session Info:", "one or more field not found in JSON data");
                    ex.printStackTrace();
                }
            }
        }
    }
}