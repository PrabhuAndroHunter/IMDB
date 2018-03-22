package com.imdb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.imdb.R;
import com.imdb.activity.TrailerPlayerActivity;
import com.imdb.model.Movie;
import com.imdb.model.Trailer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prabhu on 20/3/18.
 */
public class RecyclerViewTrailerAdapter extends RecyclerView.Adapter <RecyclerViewTrailerAdapter.MyViewAdapter> {// Recyclerview will extend to
    private final String TAG = RecyclerViewTrailerAdapter.class.toString();
    Context context;
    private int tag;
    List <Trailer> trailerList = new ArrayList <Trailer>();
    public static final int TOP_RATED_MOVIE = 1, NOW_PLAYING = 2, UPCOMING_MOVIE = 3, POPULAR_MOVIE = 4;
    private TrailerPlayerActivity parentActivity;

    public RecyclerViewTrailerAdapter(Context context, int tag) {
        this.context = context;
        parentActivity = (TrailerPlayerActivity) context;
        this.tag = tag;
    }

    @Override
    public MyViewAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new MyViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(MyViewAdapter holder, int position) {
        final Trailer currentTransaction = trailerList.get(position);
        holder.mTitleTv.setText(currentTransaction.getName());
        Glide.with(context)
                .load("http://img.youtube.com/vi/"+currentTransaction.getVedioLink()+"/0.jpg")
                .placeholder(R.drawable.ic_imdb_logo)
                .crossFade()
                .into(holder.mThumbImv);
        Log.d(TAG, "onBindViewHolder: "+"http://img.youtube.com/vi/"+currentTransaction.getVedioLink()+"/0.jpg");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: vedio link " + currentTransaction.getVedioLink());
                parentActivity.updateTrailer(currentTransaction.getVedioLink());
            }
        });
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }


    class MyViewAdapter extends RecyclerView.ViewHolder {
        private TextView mTitleTv;
        private ImageView mThumbImv;

        public MyViewAdapter(View itemView) {
            super(itemView);
            mTitleTv = (TextView) itemView.findViewById(R.id.title);
            mThumbImv = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    public void refreshUI(List <Trailer> trailerList) {
        this.trailerList.clear();
        this.trailerList = trailerList;
        notifyDataSetChanged();
    }
}
