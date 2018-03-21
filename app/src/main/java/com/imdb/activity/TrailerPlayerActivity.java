package com.imdb.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.imdb.network.Url;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TrailerPlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private final String TAG = TrailerPlayerActivity.class.toString();
    YouTubePlayer player;
    String videoKey = "";
    YouTubePlayerView playerView;
    Bundle bundle;
    ArrayList <HashMap <String, String>> videoList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer_player);
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

        int i = 0;
        for (HashMap <String, String> map : videoList) {

            for (Map.Entry <String, String> mapEntry : map.entrySet()) {
                String key = mapEntry.getKey();
                final String value = mapEntry.getValue();
                videoKey = value;
                Log.d(TAG, "onCreate: videoKey " + videoKey);
                return;
            }
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean b) {
        Log.i("player", "player is playing");
        if (null == player) return;
        if (videoKey.isEmpty()) {
            Toast.makeText(this, "Trailer is not Available yet", Toast.LENGTH_SHORT).show();
            return;
        }
        // Start buffering
        if (!b) {
            this.player=player;
            player.cueVideo(videoKey);
        }
        player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
        player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
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
        });
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


    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, "player_error", Toast.LENGTH_LONG).show();
    }

}
