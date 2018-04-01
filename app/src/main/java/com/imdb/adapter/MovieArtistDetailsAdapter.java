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
import com.imdb.activity.HomeScreenActivity;
import com.imdb.activity.MovieDetailsActivity;
import com.imdb.activity.TrailerPlayerActivity;
import com.imdb.application.Application;
import com.imdb.model.Cast;
import com.imdb.model.Crew;
import com.imdb.model.Movie;
import com.imdb.util.RoundedImageView;

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
public class MovieArtistDetailsAdapter extends RecyclerView.Adapter <MovieArtistDetailsAdapter.MyViewAdapter> {// Recyclerview will extend to
    private final String TAG = MovieArtistDetailsAdapter.class.toString();
    private Context context;
    private MovieDetailsActivity parentActivity;
    private int tag;
    List <Cast> castList = new ArrayList <Cast>();
    List <Crew> crewList = new ArrayList <Crew>();
    public static final int CAST = 1, CREW = 2;

    public MovieArtistDetailsAdapter(Context context, int tag) {
        this.context = context;
        this.parentActivity = (MovieDetailsActivity) context;
        this.tag = tag;
    }

    @Override
    public MyViewAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist_details, parent, false);
        return new MyViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(MyViewAdapter holder, int position) {
        if (tag == CAST) {
            final Cast cast = castList.get(position);
            if (cast != null) {
                holder.mNameTv.setText(cast.getName());
                Log.d(TAG, "onBindViewHolder: prabhu " + cast.getProfilePath());
                if (cast.getGender().equalsIgnoreCase("MALE")) {
                    Glide.with(context)
                            .load(cast.getProfilePath())
                            .crossFade()
                            .placeholder(R.drawable.ic_user_male)
                            .into(holder.mThumbImv);
                    holder.mPossitionTv.setText(cast.getCharacter());
                } else {
                    Glide.with(context)
                            .load(cast.getProfilePath())
                            .crossFade()
                            .placeholder(R.drawable.ic_user_female)
                            .into(holder.mThumbImv);
                    holder.mPossitionTv.setText(cast.getCharacter());
                }
            }
        } else {
            final Crew crew = crewList.get(position);
            if (crew != null) {
                holder.mNameTv.setText(crew.getName());
                Log.d(TAG, "onBindViewHolder: prabhu crew " + crew.getProfilePath());
                if (crew.getGender().equalsIgnoreCase("MALE")) {
                    Glide.with(context)
                            .load(crew.getProfilePath())
                            .crossFade()
                            .placeholder(R.drawable.ic_user_male)
                            .into(holder.mThumbImv);
                    holder.mPossitionTv.setText(crew.getJob());
                } else {
                    Glide.with(context)
                            .load(crew.getProfilePath())
                            .crossFade()
                            .placeholder(R.drawable.ic_user_female)
                            .into(holder.mThumbImv);
                    holder.mPossitionTv.setText(crew.getJob());
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (tag == CAST)
            return castList.size();
        else
            return crewList.size();
    }


    class MyViewAdapter extends RecyclerView.ViewHolder {
        private TextView mNameTv, mPossitionTv;
        private ImageView mThumbImv;

        public MyViewAdapter(View itemView) {
            super(itemView);
            mNameTv = (TextView) itemView.findViewById(R.id.text_view_name);
            mPossitionTv = (TextView) itemView.findViewById(R.id.text_view_possition);
            mThumbImv = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    public void refreshUI(Object dataObj) {
        if (tag == CAST) {
            this.castList.clear();
            this.castList = (List <Cast>) dataObj;
        } else {
            this.crewList.clear();
            this.crewList = (List <Crew>) dataObj;
        }
        notifyDataSetChanged();
    }
}
