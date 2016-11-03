package com.mabrouk.moviedb;

import android.support.v4.app.Fragment;

import com.mabrouk.moviedb.movie.MoviesMainFragment;
import com.mabrouk.moviedb.network.ApiInfo;
import com.mabrouk.moviedb.people.PeopleListFragment;
import com.mabrouk.moviedb.people.PeoplePagesLoader;
import com.mabrouk.moviedb.people.PeopleService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by VPN on 11/2/2016.
 */

public class MainSectionsFactory {
    private MoviesMainFragment moviesMainFragment = new MoviesMainFragment();
    private PeopleListFragment peopleListFragment;

    public Fragment fragmentForPage(int pageIndex) {
        switch (pageIndex) {
            case 0:
                return moviesMainFragment;
            case 2:
                return getPeopleListFragment();
            default:
                return new Fragment();
        }
    }

    private PeopleListFragment getPeopleListFragment() {
        if(peopleListFragment == null) {
            PeopleService service = new Retrofit.Builder().baseUrl(ApiInfo.BASE_URL)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(PeopleService.class);
            PeoplePagesLoader peoplePagesLoader = new PeoplePagesLoader(service::getPopularPeople);
            peopleListFragment = new PeopleListFragment();
            peopleListFragment.setPagesLoader(peoplePagesLoader);
        }
        return peopleListFragment;
    }
}
