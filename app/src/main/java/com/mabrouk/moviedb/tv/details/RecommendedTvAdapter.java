package com.mabrouk.moviedb.tv.details;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.common.RatingUtils;
import com.mabrouk.moviedb.tv.Tv;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by User on 11/16/2016.
 */

public class RecommendedTvAdapter extends RecyclerView.Adapter<RecommendedTvAdapter.RecommendedTvViewHolder>{
    List<Tv> shows;

    public RecommendedTvAdapter(List<Tv> shows) {
        this.shows = shows;
    }

    @Override
    public RecommendedTvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_recommended_movie, parent, false);
        return new RecommendedTvViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(RecommendedTvViewHolder holder, int position) {
        Tv tv = shows.get(position);
        holder.itemView.setTag(tv);
        holder.title.setText(tv.getName());
        holder.rating.setText(tv.getDisplayableRating());
        RatingUtils.loadRatingDrawableIntoView(tv.getRating(), holder.rating);
        Picasso.with(holder.itemView.getContext()).load(tv.getLargeThumbnailUrl()).into(holder.thumbnail);
        holder.itemView.setOnClickListener(view ->
            TvDetailsActivity.startInstance(tv, (Activity) view.getContext())
        );
    }

    @Override
    public int getItemCount() {
        return shows.size();
    }

    static class RecommendedTvViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView title;
        TextView rating;

        public RecommendedTvViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            title = (TextView) itemView.findViewById(R.id.movie_title);
            rating = (TextView) itemView.findViewById(R.id.rating);
        }
    }
}
