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

public class MovieCreditsFragment extends Fragment {
    Movie movie;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    Subscription subscription;

    public static MovieCreditsFragment newInstance(Movie movie) {
        MovieCreditsFragment fragment = new MovieCreditsFragment();
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

        subscription = ServiceProvider.getService().getMovieCredits(movie.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(MovieCredits::filter)
                .subscribe(this::gotCredits, this::gotError);

        return root;
    }

    private void gotCredits(MovieCredits credits) {
        recyclerView.setAdapter(new MovieCreditsAdapter(credits));
        progressBar.setVisibility(View.GONE);
    }

    private void gotError(Throwable e) {
        e.printStackTrace();
    }
}
