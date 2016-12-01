package com.mabrouk.moviedb.tv.details;

import android.view.View;

import com.mabrouk.moviedb.movie.details.HorizontalListFragment;
import com.mabrouk.moviedb.movie.details.MovieVideo;
import com.mabrouk.moviedb.movie.details.MovieVideosAdapter;
import com.mabrouk.moviedb.tv.Tv;
import com.mabrouk.moviedb.tv.api.TvServiceProvider;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by VPN on 11/17/2016.
 */

public class TvVideosFragment extends HorizontalListFragment {
    Tv tv;
    public static TvVideosFragment createInstance(Tv tv) {
        TvVideosFragment fragment = new TvVideosFragment();
        fragment.tv = tv;
        return fragment;
    }

    @Override
    protected void subscribeToService() {
        TvServiceProvider.getService().getVideos(tv.getId())
                .map(MovieVideo.VideoList::getResults)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(videos -> {
                    recyclerView.setAdapter(new MovieVideosAdapter(videos));
                    progressBar.setVisibility(View.GONE);
                });
    }

    @Override
    protected String getErrorMessage() {
        return "Error\nTap to retry";
    }

    @Override
    protected String getEmptyMessage() {
        return "No Videos Available For This Show";
    }
}
