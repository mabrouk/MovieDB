package com.mabrouk.moviedb.people.api;

import com.mabrouk.moviedb.network.ApiInfo;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by User on 11/7/2016.
 */

public class PeopleServiceProvider {
    private static PeopleService service = new Retrofit.Builder().baseUrl(ApiInfo.BASE_URL)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(PeopleService.class);

    public static PeopleService getService() {
        return service;
    }
}
