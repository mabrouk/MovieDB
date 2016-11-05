package com.mabrouk.moviedb.movie.details;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.movie.Movie;
import com.mabrouk.moviedb.movie.api.ServiceProvider;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MovieCreditsFragment extends HorizontalListFragment {

    public static MovieCreditsFragment newInstance(Movie movie) {
        MovieCreditsFragment fragment = new MovieCreditsFragment();
        fragment.movie = movie;
        return fragment;
    }

    private void gotCredits(MovieCredits credits) {
        recyclerView.setAdapter(new MovieCreditsAdapter(credits));
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void subscribeToService() {
        subscription = ServiceProvider.getService().getMovieCredits(movie.getId())
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
