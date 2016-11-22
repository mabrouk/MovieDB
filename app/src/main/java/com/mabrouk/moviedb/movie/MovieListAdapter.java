package com.mabrouk.moviedb.movie;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mabrouk.moviedb.common.DataBag;
import com.mabrouk.moviedb.common.PagingAdapter;
import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.movie.details.MovieDetailsActivity;
import com.mabrouk.moviedb.network.MediaUrlBuilder;
import com.squareup.picasso.Picasso;

/**
 * Created by VPN on 11/2/2016.
 */

public class MovieListAdapter extends PagingAdapter<Movie, MovieListAdapter.MovieViewHolder> {
    int thumbWidth, thumbHeight;
    public MovieListAdapter(Context context) {
        super(context);
        thumbHeight = (int) context.getResources().getDimension(R.dimen.movie_thumb_height);
        thumbWidth = (int) context.getResources().getDimension(R.dimen.movie_thumb_width);
    }

    @Override
    public MovieViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        View row = inflater.inflate(R.layout.row_movie, parent, false);
        row.setOnClickListener(this::rowClicked);
        return new MovieViewHolder(row);
    }

    void rowClicked(View row) {
        Movie movie = (Movie) row.getTag();
        MovieDetailsActivity.startMovieDetailsActivity(row.getContext(), movie);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, Movie movie) {
        holder.itemView.setTag(movie);

        holder.title.setText(movie.getTitle());
        holder.releaseDate.setText(movie.getFormattedReleaseDate());
        holder.overview.setText(movie.getOverview());
        String thumbUrl = new MediaUrlBuilder(movie.getPosterPath())
                .addSize(thumbWidth, thumbHeight)
                .addType(MediaUrlBuilder.TYPE_POSTER)
                .build();
        Picasso.with(holder.itemView.getContext()).load(thumbUrl).into(holder.thumbnail);
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView releaseDate;
        TextView overview;
        ImageView thumbnail;

        public MovieViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.movie_title);
            releaseDate = (TextView) itemView.findViewById(R.id.release_date);
            overview = (TextView) itemView.findViewById(R.id.overview);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
        }
    }
}
