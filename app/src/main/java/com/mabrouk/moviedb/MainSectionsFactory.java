package com.mabrouk.moviedb;

import android.support.v4.app.Fragment;

import com.mabrouk.moviedb.genres.GenresFragment;
import com.mabrouk.moviedb.movie.MoviesMainFragment;
import com.mabrouk.moviedb.people.PeopleMainFragment;
import com.mabrouk.moviedb.tv.TvMainFragment;

/**
 * Created by VPN on 11/2/2016.
 */

public class MainSectionsFactory {
    private MoviesMainFragment moviesMainFragment = new MoviesMainFragment();
    private PeopleMainFragment peopleMainFragment = new PeopleMainFragment();
    private TvMainFragment tvMainFragment = new TvMainFragment();
    private GenresFragment movieGenresFragment = new GenresFragment();

    public Fragment fragmentForPage(int pageIndex) {
        switch (pageIndex) {
            case 0:
                return moviesMainFragment;
            case 1:
                return tvMainFragment;
            case 2:
                return peopleMainFragment;
            case 3:
                return movieGenresFragment;
            default:
                //won't happen
                throw new IllegalStateException("WTF");
        }
    }
}
