package com.mabrouk.moviedb.movie.details;

import android.view.View;

import com.mabrouk.moviedb.movie.Movie;
import com.mabrouk.moviedb.movie.api.MovieServiceProvider;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MovieVideosFragment extends HorizontalListFragment {
   public static MovieVideosFragment createInstance(Movie movie) {
       MovieVideosFragment fragment = new MovieVideosFragment();
       fragment.movie = movie;
       return fragment;
   }

    @Override
    protected void subscribeToService() {
        subscription = MovieServiceProvider.getService().getMovieVideos(movie.getId())
                .map(MovieVideo.VideoList::getResults)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getVideos, this::gotError);
    }

    @Override
    protected String getErrorMessage() {
        return "Couldn't load videos\nTap to retry";
    }

    @Override
    protected String getEmptyMessage() {
        return "No Videos Available";
    }

    void getVideos(List<MovieVideo> videos) {
        progressBar.setVisibility(View.GONE);
        if(videos.size() > 0)
            recyclerView.setAdapter(new MovieVideosAdapter(videos));
        else
            showEmptyMessage();
    }

}
