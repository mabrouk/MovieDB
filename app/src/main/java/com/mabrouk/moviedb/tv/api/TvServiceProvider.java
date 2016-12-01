package com.mabrouk.moviedb.tv.api;

import com.mabrouk.moviedb.network.ApiInfo;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by User on 11/12/2016.
 */

public class TvServiceProvider {
    private static TvService service = new Retrofit.Builder().baseUrl(ApiInfo.BASE_URL)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(TvService.class);

    public static TvService getService() {
        return service;
    }
}
