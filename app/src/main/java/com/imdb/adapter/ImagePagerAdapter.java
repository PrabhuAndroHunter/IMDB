package com.imdb.adapter;

import android.app.ProgressDialog;
import android.content.Context;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.imdb.R;
import com.imdb.activity.HomeScreenActivity;
import com.imdb.activity.TrailerPlayerActivity;
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
 * Created by prabhu on 20/3/18.
 */

public class ImagePagerAdapter extends PagerAdapter {
    private final String TAG = ImagePagerAdapter.class.toString();

    Context mContext;
    LayoutInflater mLayoutInflater;
    List <Movie> topFivePosters = new ArrayList <Movie>();
    private HomeScreenActivity parentActivity;

    public ImagePagerAdapter(Context context) {
        mContext = context;
        parentActivity = (HomeScreenActivity) context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return topFivePosters.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_pager, container, false);
        final Movie curMovie = topFivePosters.get(position);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        TextView textView = (TextView) itemView.findViewById(R.id.textview_movie_name);

        Glide.with(mContext)
                .load(curMovie.getBanner())
                .crossFade()
                .into(imageView);
        textView.setText(curMovie.getTitle());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + curMovie.getTitle());
                Application.setCurrentPlayingMovie(curMovie);
//                parentActivity.startActivity(new Intent(parentActivity, TrailerPlayerActivity.class));
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

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    public void setTopFivePosters(List <Movie> topFivePosters) {
        this.topFivePosters = topFivePosters;
        notifyDataSetChanged();
    }

    private class FetchNowPlayingMoviesData extends AsyncTask <String, Void, Void> {
        String tag;
        ProgressDialog progressDialog = new ProgressDialog(mContext);
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
                Toast.makeText(mContext, error, Toast.LENGTH_LONG).show();
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