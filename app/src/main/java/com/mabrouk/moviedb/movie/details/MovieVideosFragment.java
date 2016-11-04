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

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MovieVideosFragment extends Fragment {
    RecyclerView recyclerView;
    Subscription subscription;
    Movie movie;
    ProgressBar progressBar;
    MovieVideosAdapter adapter;

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movie_details_horizontal_list, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        adapter = new MovieVideosAdapter();
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        subscription = ServiceProvider.getService().getMovieVideos(movie.getId())
                .map(MovieVideo.VideoList::getResults)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getVideos);
        return root;
    }

    void getVideos(List<MovieVideo> videos) {
        progressBar.setVisibility(View.GONE);
        adapter.setVideos(videos);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null)
            subscription.unsubscribe();
    }
}
