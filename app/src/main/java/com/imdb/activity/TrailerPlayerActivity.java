package com.imdb.activity;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.imdb.R;
import com.imdb.adapter.RecyclerViewAdapter;
import com.imdb.adapter.RecyclerViewTrailerAdapter;
import com.imdb.application.Application;
import com.imdb.model.Movie;
import com.imdb.model.Trailer;
import com.imdb.network.Url;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrailerPlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private final String TAG = TrailerPlayerActivity.class.toString();
    YouTubePlayerView playerView;
    private TextView mVoteTv, mRatingTv, mRateThisTv, mShareTv, mWatchLater, mDescriptionTv, mReleaseDate;
    YouTubePlayer player;
    private RecyclerView mTrailerRv;
    String videoKey = "";
    Bundle bundle;
    ArrayList <HashMap <String, String>> videoList;
    private List <Trailer> trailerList = new ArrayList <Trailer>();
    private Movie currentMovie;
    private RecyclerViewTrailerAdapter trailersAdapter;
    private boolean isWatchLater = false;


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

        mTrailerRv.setLayoutManager(new LinearLayoutManager(TrailerPlayerActivity.this, LinearLayoutManager.HORIZONTAL, false));

        Log.d(TAG, "onCreate: ");
        bundle = getIntent().getBundleExtra("movie");
        videoList = (ArrayList <HashMap <String, String>>) getIntent().getSerializableExtra("videoIdList");
        Log.d(TAG, "onCreate: videoList" + videoList.size());
        final YouTubeInitializationResult result = YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(this);
        if (result != YouTubeInitializationResult.SUCCESS) {
            //If there are any issues we can show an error dialog.
            result.getErrorDialog(this, 0).show();
            this.finish();
        }
        playerView = (YouTubePlayerView) findViewById(R.id.youtube_playerview);
        playerView.initialize(Url.YOUTUBE_API_KEY, TrailerPlayerActivity.this);

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

            }
        });

        mShareTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        loadTrailers();
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
      /*  player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
            @Override
            public void onAdStarted() {
            }

            @Override
            public void onError(YouTubePlayer.ErrorReason arg0) {
            }

            @Override
            public void onLoaded(String arg0) {
            }

            @Override
            public void onLoading() {
            }

            @Override
            public void onVideoEnded() {
            }

            @Override
            public void onVideoStarted() {
            }
        });*/
/*
        player.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
            @Override
            public void onBuffering(boolean arg0) {
            }

            @Override
            public void onPaused() {
            }

            @Override
            public void onPlaying() {
            }

            @Override
            public void onSeekTo(int arg0) {
            }

            @Override
            public void onStopped() {
            }
        });
*/


    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, "player_error", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentMovie = Application.getCurrentPlayingMovie();
        mVoteTv.setText("" + currentMovie.getVoteCount());
        mRatingTv.setText(currentMovie.getRating() + "/10");
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
}

