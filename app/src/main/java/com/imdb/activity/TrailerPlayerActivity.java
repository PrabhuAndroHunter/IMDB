package com.imdb.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;
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
import org.w3c.dom.Text;

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

public class TrailerPlayerActivity extends BaseActivity implements YouTubePlayer.OnInitializedListener, ResponseListener {

    private final String TAG = TrailerPlayerActivity.class.toString();
    private TextView mVoteTv, mRatingTv, mRateThisTv, mShareTv, mWatchLater, mDescriptionTv, mReleaseDate, mMovieStatus;
    YouTubePlayer player;
    private RecyclerView mTrailerRv;
    String videoKey = "";
    Bundle bundle;
    ArrayList <HashMap <String, String>> videoList;
    private List <Trailer> trailerList = new ArrayList <Trailer>();
    private Movie currentMovie;
    private RecyclerViewTrailerAdapter trailersAdapter;
    private boolean isWatchLater = false;
    private static float ratingOne, ratingTwo;
    private String imdbId, status;
    String guest_session_id = "";


    RatingBar mRatingBarFirst, mRatingBarSecond;
    Button mSubmitBtn;
    TextView mUserRatingTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer_player);
        mTrailerRv = (RecyclerView) findViewById(R.id.recycler_view_trailer);
        mVoteTv = (TextView) findViewById(R.id.text_view_vote);
        mRatingTv = (TextView) findViewById(R.id.text_view_votestar);
        mRateThisTv = (TextView) findViewById(R.id.text_view_rateings);
        mShareTv = (TextView) findViewById(R.id.text_view_share);
        mWatchLater = (TextView) findViewById(R.id.text_view_watchlist);
        mDescriptionTv = (TextView) findViewById(R.id.text_view_movie_description);
        mReleaseDate = (TextView) findViewById(R.id.text_view_release_date);
        mMovieStatus = (TextView) findViewById(R.id.text_view_staus);
        mTrailerRv.setLayoutManager(new LinearLayoutManager(TrailerPlayerActivity.this, LinearLayoutManager.HORIZONTAL, false));

        bundle = getIntent().getBundleExtra("movie");
        videoList = (ArrayList <HashMap <String, String>>) getIntent().getSerializableExtra("videoIdList");
        final YouTubeInitializationResult result = YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(this);
        if (result != YouTubeInitializationResult.SUCCESS) {
            //If there are any issues we can show an error dialog.
            result.getErrorDialog(this, 0).show();
            this.finish();
        }
        YouTubePlayerSupportFragment frag =
                (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_playerview);
        frag.initialize(Url.YOUTUBE_API_KEY, this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("IMDB Player");

        for (HashMap <String, String> map : videoList) {
            for (Map.Entry <String, String> mapEntry : map.entrySet()) {
                String name = mapEntry.getKey();
                final String vedioLink = mapEntry.getValue();
                Trailer trailer = new Trailer(name, vedioLink);
                trailerList.add(trailer);
            }
        }
        if (trailerList.size() > 0)
            videoKey = trailerList.get(0).getVedioLink();

        mRateThisTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingdialog();
            }
        });

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
        loadTrailers();
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
        currentMovie = Application.getCurrentPlayingMovie();
        RequestHelper.getMovieDetails(currentMovie.getMovieId(), this);
        mVoteTv.setText("" + currentMovie.getVoteCount());
        mRatingTv.setText(currentMovie.getRating());
        mReleaseDate.setText(currentMovie.getReleaseDate());
        mDescriptionTv.setText(currentMovie.getDescription());
        Log.d(TAG, "onStart: " + currentMovie.getTitle());
    }

    private void loadTrailers() {
        trailersAdapter = new RecyclerViewTrailerAdapter(this, RecyclerViewAdapter.NOW_PLAYING);
        mTrailerRv.setAdapter(trailersAdapter);
        trailersAdapter.refreshUI(trailerList);
    }

    public void updateTrailer(String videoKey) {
        this.videoKey = videoKey;
        player.cueVideo(videoKey);
        player.play();
    }

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

    @Override
    public void searchFail(String error, String tag) {

    }

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

        mRatingBarFirst.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                String ratedValue = String.valueOf(ratingBar.getRating());
                float ratingOne = Float.valueOf(ratedValue);
                updateRatingFirst(ratingOne);
                Log.d(TAG, "onRatingChanged: " + ratedValue);
            }
        });

        mRatingBarSecond.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                String ratedValue = String.valueOf(ratingBar.getRating());
                float ratingTwo = Float.valueOf(ratedValue);
                updateRatingSecond(ratingTwo);
                Log.d(TAG, "onRatingChanged: " + ratedValue);
            }
        });

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PostRatingData().execute(String.valueOf(ratingTwo));
                ratingDialog.dismiss();
            }
        });
        ratingDialog.show();
    }

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

            Log.d(TAG, "onPostExecute: "+result);
            if (result.equalsIgnoreCase("Fail")) {
                Toast.makeText(getApplicationContext(), result,
                        Toast.LENGTH_LONG).show();
            }else {
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

