package com.mabrouk.moviedb.movie.details;


import android.view.View;

import com.mabrouk.moviedb.common.ResultList;
import com.mabrouk.moviedb.movie.Movie;
import com.mabrouk.moviedb.movie.api.ServiceProvider;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RecommendedMoviesFragment extends HorizontalListFragment {

    public static RecommendedMoviesFragment newInstance(Movie movie) {
        RecommendedMoviesFragment fragment = new RecommendedMoviesFragment();
        fragment.movie = movie;
        return fragment;
    }

    @Override
    protected void subscribeToService() {
        subscription = ServiceProvider.getService().getMovieRecommendations(movie.getId())
                .map(ResultList::getResults)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::gotRecommendedMovies, this::gotError);
    }

    @Override
    protected String getErrorMessage() {
        return "Couldn't load recommended movies\nTap to retry";
    }

    @Override
    protected String getEmptyMessage() {
        return "No Recommended Movies Available";
    }

    private void gotRecommendedMovies(List<Movie> movies) {
        progressBar.setVisibility(View.GONE);
        recyclerView.setAdapter(new RecommendedMoviesAdapter(movies, getResources()));
    }


}
