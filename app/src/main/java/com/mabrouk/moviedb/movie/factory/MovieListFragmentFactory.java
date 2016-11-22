package com.mabrouk.moviedb.movie.factory;

import android.support.v4.app.Fragment;

import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.common.PagesLoader;
import com.mabrouk.moviedb.movie.MovieListFragment;
import com.mabrouk.moviedb.movie.api.MovieServiceProvider;

/**
 * Created by VPN on 11/2/2016.
 */

public class MovieListFragmentFactory {
    public final static int SECTIONS_COUNT = 4;

    private MovieListFragment popularFragment;
    private MovieListFragment topRatedFragment;
    private MovieListFragment nowPlayingFragment;
    private MovieListFragment upcomingFragment;

    public Fragment fragmentForPage(int pageIndex) {
        switch (pageIndex) {
            case 0:
                return getPopularFragment();
            case 1:
                return getTopRatedFragment();
            case 2:
                return getNowPlayingFragment();
            case 3:
                return getUpcomingFragment();
            default:
                return getPopularFragment();
        }
    }

    public static int pageTitleRes(int pageIndex) {
        final int[] titles = new int[] {R.string.label_popular, R.string.label_top_rated, R.string.label_now_playing,
        R.string.label_upcoming};
        return titles[pageIndex];
    }

    private MovieListFragment getPopularFragment() {
        if(popularFragment == null) {
            popularFragment = new MovieListFragment();
            popularFragment.setPagesLoader(new PagesLoader<>(MovieServiceProvider.getService()::popular));
        }
        return popularFragment;
    }

    private MovieListFragment getTopRatedFragment() {
        if(topRatedFragment == null) {
            topRatedFragment = new MovieListFragment();
            topRatedFragment.setPagesLoader(new PagesLoader<>(MovieServiceProvider.getService()::topRated));
        }
        return topRatedFragment;
    }

    private MovieListFragment getNowPlayingFragment() {
        if(nowPlayingFragment == null) {
            nowPlayingFragment = new MovieListFragment();
            nowPlayingFragment.setPagesLoader(new PagesLoader<>(MovieServiceProvider.getService()::nowPlayingMovies));
        }
        return nowPlayingFragment;
    }

    private MovieListFragment getUpcomingFragment() {
        if(upcomingFragment == null) {
            upcomingFragment = new MovieListFragment();
            upcomingFragment.setPagesLoader(new PagesLoader<>(MovieServiceProvider.getService()::nowPlayingMovies));
        }
        return upcomingFragment;
    }
}
