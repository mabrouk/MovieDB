package com.mabrouk.moviedb.movie;

import android.content.Context;

import com.mabrouk.moviedb.common.ListFragment;
import com.mabrouk.moviedb.common.PagingAdapter;


public class MovieListFragment extends ListFragment<Movie> {

    @Override
    protected PagingAdapter<Movie, ?> initAdapter(Context context) {
        return new MovieListAdapter(context);
    }
}
