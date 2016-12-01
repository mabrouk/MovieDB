package com.mabrouk.moviedb.tv;

import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.common.PagesLoader;
import com.mabrouk.moviedb.tv.api.TvServiceProvider;

/**
 * Created by VPN on 11/3/2016.
 */

public class TvListFragmentFactory {
    public final static int SECTIONS_COUNT = 3;

    private TvListFragment popularTvFragment;
    private TvListFragment topRatedTvFragment;
    private TvListFragment airingTvFragment;
    private int[] titleRes = new int[]{R.string.label_popular, R.string.label_top_rated, R.string.label_airing};

    public TvListFragment fragmentForPage(int pagePosition) {
        switch (pagePosition) {
            case 0:
                return getPopularTvFragment();
            case 1:
                return getTopRatedTvFragment();
            case 2:
                return getAiringTvFragment();
            default:
                return getPopularTvFragment();
        }
    }

    public int titleForPage(int pagePosition) {
        return titleRes[pagePosition];
    }

    private TvListFragment getPopularTvFragment() {
        if(popularTvFragment == null) {
            popularTvFragment = new TvListFragment();
            popularTvFragment.setPagesLoader(new PagesLoader(TvServiceProvider.getService()::getPopularTv));
        }
        return popularTvFragment;
    }

    private TvListFragment getAiringTvFragment() {
        if(airingTvFragment == null) {
            airingTvFragment = new TvListFragment();
            airingTvFragment.setPagesLoader(new PagesLoader(TvServiceProvider.getService()::getAiringTv));
        }
        return airingTvFragment;
    }

    private TvListFragment getTopRatedTvFragment() {
        if(topRatedTvFragment== null) {
            topRatedTvFragment = new TvListFragment();
            topRatedTvFragment.setPagesLoader(new PagesLoader(TvServiceProvider.getService()::getTopRatedTv));
        }
        return topRatedTvFragment;
    }

}
