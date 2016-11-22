package com.mabrouk.moviedb.movie.details;


import android.view.View;

import com.mabrouk.moviedb.movie.Movie;
import com.mabrouk.moviedb.movie.api.MovieServiceProvider;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MovieCreditsFragment extends HorizontalListFragment {

    public static MovieCreditsFragment newInstance(Movie movie) {
        MovieCreditsFragment fragment = new MovieCreditsFragment();
        fragment.movie = movie;
        return fragment;
    }

    private void gotCredits(MovieCredits credits) {
        recyclerView.setAdapter(new MovieCreditsAdapter(credits, getResources()));
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void subscribeToService() {
        subscription = MovieServiceProvider.getService().getMovieCredits(movie.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(MovieCredits::filter)
                .subscribe(this::gotCredits, this::gotError);
    }

    @Override
    protected String getErrorMessage() {
        return "Couldn't load cast\nTap to retry";
    }

    @Override
    protected String getEmptyMessage() {
        return "No Cast Available";
    }
}
