package com.mabrouk.moviedb;

import android.support.v4.app.Fragment;

import com.mabrouk.moviedb.common.PagesLoader;
import com.mabrouk.moviedb.movie.MoviesMainFragment;
import com.mabrouk.moviedb.network.ApiInfo;
import com.mabrouk.moviedb.people.PeopleListFragment;
import com.mabrouk.moviedb.people.PeopleMainFragment;
import com.mabrouk.moviedb.people.PeopleService;
import com.mabrouk.moviedb.tv.TvMainFragment;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by VPN on 11/2/2016.
 */

public class MainSectionsFactory {
    private MoviesMainFragment moviesMainFragment = new MoviesMainFragment();
    private PeopleMainFragment peopleMainFragment = new PeopleMainFragment();
    private TvMainFragment tvMainFragment = new TvMainFragment();

    public Fragment fragmentForPage(int pageIndex) {
        switch (pageIndex) {
            case 0:
                return moviesMainFragment;
            case 1:
                return tvMainFragment;
            case 2:
                return peopleMainFragment;
            default:
                //won't happen
                throw new IllegalStateException("WTF");
        }
    }
}
