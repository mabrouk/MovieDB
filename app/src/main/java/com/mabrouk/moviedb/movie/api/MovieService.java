package com.mabrouk.moviedb.movie.api;

import com.mabrouk.moviedb.common.ResultList;
import com.mabrouk.moviedb.movie.Movie;
import com.mabrouk.moviedb.network.ApiInfo;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by VPN on 11/1/2016.
 */

public interface MovieService {
    @GET("movie/now_playing?api_key=" + ApiInfo.API_KEY)
    Observable<ResultList<Movie>> nowPlayingMovies(@Query("page") int page);

    @GET("movie/popular?api_key=" + ApiInfo.API_KEY)
    Observable<ResultList<Movie>> popular(@Query("page") int page);

    @GET("movie/top_rated?api_key=" + ApiInfo.API_KEY)
    Observable<ResultList<Movie>> topRated(@Query("page") int page);

    @GET("movie/upcoming?api_key=" + ApiInfo.API_KEY)
    Observable<ResultList<Movie>> upcoming(@Query("page") int page);

    @GET("search/movie?api_key=" + ApiInfo.API_KEY)
    Observable<ResultList<Movie>> search(@Query("query") String query, @Query("page") int page);
}
