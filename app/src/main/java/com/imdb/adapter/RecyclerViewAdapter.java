package com.imdb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.imdb.R;
import com.imdb.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prabhu on 20/03/18.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter <RecyclerViewAdapter.MyViewAdapter> {// Recyclerview will extend to
    private final String TAG = RecyclerViewAdapter.class.toString();
    Context context;
    private int tag;
    List <Movie> movieList = new ArrayList <Movie>();
    public static final int TOP_RATED_MOVIE = 1, NOW_PLAYING = 2, UPCOMING_MOVIE = 3, POPULAR_MOVIE = 4;


    public RecyclerViewAdapter(Context context, int tag) {
        this.context = context;
        this.tag = tag;
    }

    @Override
    public MyViewAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new MyViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(MyViewAdapter holder, int position) {
        final Movie currentTransaction = movieList.get(position);
        holder.mTitleTv.setText(currentTransaction.getTitle());
        Glide.with(context)
                .load(currentTransaction.getPoster())
                .crossFade()
                .placeholder(R.drawable.ic_imdb_logo)
                .into(holder.mThumbImv);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
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

    public void refreshUI(List <Movie> moviesList) {
        this.movieList.clear();
        this.movieList = moviesList;
        notifyDataSetChanged();
    }
}
