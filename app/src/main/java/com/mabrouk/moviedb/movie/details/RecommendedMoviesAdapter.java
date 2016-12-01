package com.mabrouk.moviedb.movie.details;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.common.Utils.RatingUtils;
import com.mabrouk.moviedb.movie.Movie;
import com.mabrouk.moviedb.network.MediaUrlBuilder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by VPN on 11/5/2016.
 */

public class RecommendedMoviesAdapter extends RecyclerView.Adapter<RecommendedMoviesAdapter.RecommendedMoviesViewHolder>{
    List<Movie> movies;
    int thumbWidth, thumbHeight;
    public RecommendedMoviesAdapter(List<Movie> movies, Resources resources) {
        this.movies = movies;
        thumbWidth = (int) resources.getDimension(R.dimen.recommended_thum_width);
        thumbHeight = (int) resources.getDimension(R.dimen.recommended_thum_height);
    }

    @Override
    public RecommendedMoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_recommended_movie, parent, false);
        itemView.setOnClickListener(this::movieClicked);
        return new RecommendedMoviesViewHolder(itemView);
    }

    void movieClicked(View view) {
        MovieDetailsActivity.startMovieDetailsActivity(view.getContext(), (Movie) view.getTag());
    }

    @Override
    public void onBindViewHolder(RecommendedMoviesViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.itemView.setTag(movie);
        holder.title.setText(movie.getTitle());
        holder.rating.setText(movie.getDisplayableRating());
        RatingUtils.loadRatingDrawableIntoView(movie.getRating(), holder.rating);
        String thumbUrl = new MediaUrlBuilder(movie.getPosterPath())
                .addType(MediaUrlBuilder.TYPE_POSTER)
                .addSize(thumbWidth, thumbHeight)
                .build();
        Picasso.with(holder.itemView.getContext()).load(thumbUrl).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return movies.size() > 12 ? 12 : movies.size();
    }

    static class RecommendedMoviesViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView title;
        TextView rating;

        public RecommendedMoviesViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            title = (TextView) itemView.findViewById(R.id.movie_title);
            rating = (TextView) itemView.findViewById(R.id.rating);
        }
    }

}
