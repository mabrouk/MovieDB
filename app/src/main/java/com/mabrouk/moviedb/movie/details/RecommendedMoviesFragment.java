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
import com.mabrouk.moviedb.common.ResultList;
import com.mabrouk.moviedb.movie.Movie;
import com.mabrouk.moviedb.movie.api.ServiceProvider;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RecommendedMoviesFragment extends Fragment {
    RecyclerView recyclerView;
    Subscription subscription;
    Movie movie;
    ProgressBar progressBar;

    public static RecommendedMoviesFragment newInstance(Movie movie) {
        RecommendedMoviesFragment fragment = new RecommendedMoviesFragment();
        fragment.movie = movie;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movie_details_horizontal_list, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        subscription = ServiceProvider.getService().getMovieRecommendations(movie.getId())
                .map(ResultList::getResults)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::gotRecommendedMovies);
        return root;
    }

    private void gotRecommendedMovies(List<Movie> movies) {
        progressBar.setVisibility(View.GONE);
        recyclerView.setAdapter(new RecommendedMoviesAdapter(movies));
    }

}
