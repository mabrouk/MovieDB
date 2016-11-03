package com.mabrouk.moviedb.tv;

import com.mabrouk.moviedb.common.ResultList;
import com.mabrouk.moviedb.network.ApiInfo;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by VPN on 11/3/2016.
 */

public interface TvService {
    @GET("tv/top_rated?api_key=" + ApiInfo.API_KEY)
    Observable<ResultList<Tv>> getTopRatedTv(@Query("page") int page);

    @GET("tv/popular?api_key=" + ApiInfo.API_KEY)
    Observable<ResultList<Tv>> getPopularTv(@Query("page") int page);

    @GET("tv/on_the_air?api_key=" + ApiInfo.API_KEY)
    Observable<ResultList<Tv>> getAiringTv(@Query("page") int page);
}
