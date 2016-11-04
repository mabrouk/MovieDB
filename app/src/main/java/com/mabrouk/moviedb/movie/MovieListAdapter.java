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
import com.squareup.picasso.Picasso;

/**
 * Created by VPN on 11/2/2016.
 */

public class MovieListAdapter extends PagingAdapter<Movie, MovieListAdapter.MovieViewHolder> {

    public MovieListAdapter(Context context) {
        super(context);
    }

    @Override
    public MovieViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        View row = inflater.inflate(R.layout.row_movie, parent, false);
        row.setOnClickListener(this::rowClicked);
        return new MovieViewHolder(row);
    }

    void rowClicked(View row) {
        Movie movie = (Movie) row.getTag();
        DataBag.addMovieToPocket(movie);
        Intent intent = new Intent(row.getContext(), MovieDetailsActivity.class);
        intent.putExtra(MovieDetailsActivity.EXTRA_MOVIE_ID, movie.getId());
        row.getContext().startActivity(intent);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, Movie movie) {
        holder.itemView.setTag(movie);

        holder.title.setText(movie.getTitle());
        holder.releaseDate.setText(movie.getReleaseDate());
        holder.overview.setText(movie.getOverview());
        Picasso.with(holder.itemView.getContext()).load(movie.getThumbnailUrl()).into(holder.thumbnail);
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
