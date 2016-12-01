package com.mabrouk.moviedb.tv.details;

import android.view.View;

import com.mabrouk.moviedb.common.ResultList;
import com.mabrouk.moviedb.movie.details.HorizontalListFragment;
import com.mabrouk.moviedb.tv.Tv;
import com.mabrouk.moviedb.tv.api.TvServiceProvider;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by User on 11/16/2016.
 */

public class RecommendedShowsFragment extends HorizontalListFragment {
    Tv tv;
    public static RecommendedShowsFragment createInstance(Tv tv) {
        RecommendedShowsFragment fragment = new RecommendedShowsFragment();
        fragment.tv = tv;
        return fragment;
    }

    @Override
    protected void subscribeToService() {
        TvServiceProvider.getService().getRecommendedShows(tv.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(ResultList::getResults)
                .subscribe(shows -> {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setAdapter(new RecommendedTvAdapter(shows, getResources()));
                });
    }

    @Override
    protected String getErrorMessage() {
        return "";
    }

    @Override
    protected String getEmptyMessage() {
        return "No Recommended Shows Available";
    }
}
