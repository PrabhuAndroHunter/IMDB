package com.imdb.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.imdb.R;
import com.imdb.activity.TrailerPlayerActivity;
import com.imdb.activity.ViewAllMovieActivity;
import com.imdb.application.Application;
import com.imdb.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by prabhu on 20/03/18.
 */
public class ViewAllMovieAdapter extends RecyclerView.Adapter <ViewAllMovieAdapter.MyViewAdapter> {// Recyclerview will extend to
    private final String TAG = RecyclerViewAdapter.class.toString();
    private Context context;
    private ViewAllMovieActivity parentActivity;
    private int tag;
    List <Movie> movieList = new ArrayList <Movie>();
    Movie currentMovie;
    public static final int TOP_RATED_MOVIE = 1, NOW_PLAYING = 2, UPCOMING_MOVIE = 3, POPULAR_MOVIE = 4;


    public ViewAllMovieAdapter(Context context, int tag) {
        this.context = context;
        this.parentActivity = (ViewAllMovieActivity) context;
        this.tag = tag;
    }

    @Override
    public MyViewAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_all_movie, parent, false);
        return new MyViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(MyViewAdapter holder, int position) {
        final Movie curMovie = movieList.get(position);
        holder.mTitleTv.setText(curMovie.getTitle());
        Glide.with(context)
                .load(curMovie.getPoster())
                .crossFade()
                .placeholder(R.drawable.ic_imdb_logo)
                .into(holder.mThumbImv);
        holder.mReleaseDateTv.setText(curMovie.getReleaseDate());
        holder.mVoteTv.setText(""+curMovie.getVoteCount());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Application.setCurrentPlayingMovie(curMovie);
                String[] s = new String[5];

                s[0] = curMovie.getTitle();
                String urlstring = "http://api.themoviedb.org/3/movie/" + curMovie.getMovieId() + "/videos?api_key=8496be0b2149805afa458ab8ec27560c";
                s[1] = urlstring.replace(" ", "%20");
                s[2] = curMovie.getRating();
                s[3] = curMovie.getMovieId();
                s[4] = curMovie.getReleaseDate();
                new FetchNowPlayingMoviesData("TRAILER").execute(s);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }


    class MyViewAdapter extends RecyclerView.ViewHolder {
        private TextView mTitleTv, mReleaseDateTv, mVoteTv;
        private ImageView mThumbImv;

        public MyViewAdapter(View itemView) {
            super(itemView);
            mTitleTv = (TextView) itemView.findViewById(R.id.title);
            mThumbImv = (ImageView) itemView.findViewById(R.id.image);
            mReleaseDateTv = (TextView) itemView.findViewById(R.id.text_view_release_date);
            mVoteTv = (TextView) itemView.findViewById(R.id.text_view_vote);
        }
    }

    public void refreshUI(List <Movie> moviesList) {
        this.movieList.clear();
        this.movieList = moviesList;
        this.currentMovie = Application.getCurrentPlayingMovie();
        notifyDataSetChanged();
    }

    private class FetchNowPlayingMoviesData extends AsyncTask <String, Void, Void> {
        String tag;
        ProgressDialog progressDialog = new ProgressDialog(parentActivity);
        String content;
        String error;
        String title = "";
        String rating = "";
        String movieId = "";
        String year = "";
        Movie m;
        ArrayList <Movie> movieArrayList = new ArrayList <Movie>();
        ListAdapter mAdapter;

        public FetchNowPlayingMoviesData(String s) {
            tag = s;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please Wait....");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... s) {
            BufferedReader br = null;
            URL url = null;


            try {
                if (tag.equals("TRAILER")) {
                    url = new URL(s[1]);
                    title = s[0];
                    rating = s[2];
                    movieId = s[3];
                    year = s[4].substring(0, 4);
                }
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
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if (error != null) {
                Toast.makeText(parentActivity, error, Toast.LENGTH_LONG).show();
            } else {
                if (tag.equals("TRAILER")) {
                    try {
                        ArrayList <HashMap <String, String>> videoList;
                        JSONObject respone = new JSONObject(content);
                        JSONArray jsonArray = respone.getJSONArray("results");
                        videoList = new ArrayList <HashMap <String, String>>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            String type = jsonArray.getJSONObject(i).getString("type");
                            String key = jsonArray.getJSONObject(i).getString("key");

                            HashMap <String, String> map = new HashMap <>();
                            map.put(type, key);
                            videoList.add(map);

                        }

                        Bundle movieBundle = new Bundle();
                        movieBundle.putString("title", title);
                        movieBundle.putString("rating", rating);
                        movieBundle.putString("movieId", movieId);
                        movieBundle.putString("year", year);

                        Intent trailerIntent = new Intent(parentActivity, TrailerPlayerActivity.class);
                        trailerIntent.putExtra("movie", movieBundle);
                        trailerIntent.putExtra("videoIdList", videoList);
                        parentActivity.startActivity(trailerIntent);
                    } catch (JSONException ex) {
                        Log.e("Movie Info:", "one or more field not found in JSON data");
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

}
