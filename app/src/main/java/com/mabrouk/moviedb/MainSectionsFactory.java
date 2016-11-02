package com.mabrouk.moviedb;

import android.support.v4.app.Fragment;

import com.mabrouk.moviedb.movie.MoviesMainFragment;

/**
 * Created by VPN on 11/2/2016.
 */

public class MainSectionsFactory {
    private MoviesMainFragment moviesMainFragment = new MoviesMainFragment();

    public Fragment fragmentForPage(int pageIndex) {
        switch (pageIndex) {
            case 0:
                return moviesMainFragment;
            default:
                return new Fragment();
        }
    }
}
