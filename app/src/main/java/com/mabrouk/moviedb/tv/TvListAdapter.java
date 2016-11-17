package com.mabrouk.moviedb.tv;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.common.PagingAdapter;
import com.mabrouk.moviedb.tv.details.TvDetailsActivity;
import com.squareup.picasso.Picasso;

/**
 * Created by VPN on 11/3/2016.
 */

public class TvListAdapter extends PagingAdapter<Tv, TvListAdapter.TvViewHolder> {

    public TvListAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindViewHolder(TvViewHolder holder, Tv data) {
        final Activity activity = (Activity) holder.itemView.getContext();
        Picasso.with(activity).load(data.getThumbnailUrl()).into(holder.thumbnail);
        holder.title.setText(data.name);
        holder.overview.setText(data.overview);
        holder.rate.setText(data.getDisplayableRating());
        holder.itemView.setOnClickListener(view ->
                TvDetailsActivity.startInstance(data, activity));
    }

    @Override
    public TvViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.row_tv, parent, false);
        return new TvViewHolder(itemView);
    }


    static class TvViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView title;
        TextView rate;
        TextView overview;

        public TvViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            rate = (TextView) itemView.findViewById(R.id.rate);
            overview = (TextView) itemView.findViewById(R.id.overview);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
        }
    }
}
