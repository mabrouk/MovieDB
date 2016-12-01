package com.mabrouk.moviedb.movie.api;

import com.mabrouk.moviedb.network.ApiInfo;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by VPN on 11/4/2016.
 */

public class MovieServiceProvider {
    private static MovieService movieService = new Retrofit.Builder()
            .baseUrl(ApiInfo.BASE_URL)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieService.class);

    public static MovieService getService() {
        return movieService;
    }
}
