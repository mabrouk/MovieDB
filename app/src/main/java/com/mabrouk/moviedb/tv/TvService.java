package com.mabrouk.moviedb.tv;

import com.mabrouk.moviedb.common.ResultList;
import com.mabrouk.moviedb.movie.details.MovieVideo;
import com.mabrouk.moviedb.network.ApiInfo;
import com.mabrouk.moviedb.tv.season.Season;

import retrofit2.http.GET;
import retrofit2.http.Path;
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

    @GET("tv/{tv_id}?api_key=" + ApiInfo.API_KEY + "&append_to_response=credits")
    Observable<Tv> getTvDetails(@Path("tv_id") int tvId);

    @GET("tv/{tv_id}/recommendations?api_key=" + ApiInfo.API_KEY)
    Observable<ResultList<Tv>> getRecommendedShows(@Path("tv_id") int tvId);

    @GET("tv/{tv_id}/videos?api_key=" + ApiInfo.API_KEY)
    Observable<MovieVideo.VideoList> getVideos(@Path("tv_id") int tvId);

    @GET("tv/{tv_id}/season/{season_number}?api_key=" + ApiInfo.API_KEY)
    Observable<Season> getSeason(@Path("tv_id") int tvId, @Path("season_number") int seasonNumber);
}
