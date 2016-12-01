package com.mabrouk.moviedb.genres;

import com.mabrouk.moviedb.common.ResultList;
import com.mabrouk.moviedb.movie.Movie;
import com.mabrouk.moviedb.network.ApiInfo;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by VPN on 12/1/2016.
 */

public class GenresServiceProvider {
    interface GenresService {
        @GET("genre/movie/list?api_key=" + ApiInfo.API_KEY)
        Observable<GenresList> getMovieGenres();

        @GET("genre/{genre_id}/movies?api_key=" + ApiInfo.API_KEY)
        Observable<ResultList<Movie>> getMoviesByGenre(@Path("genre_id") int genreId, @Query("page") int page);

    }

    static GenresService service = new Retrofit.Builder()
            .baseUrl(ApiInfo.BASE_URL)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(GenresService.class);

    public static Observable<List<Genre>> getMovieGenres() {
        return service.getMovieGenres().map(GenresList::getGenres);
    }

    public static Observable<ResultList<Movie>> getMoviesByGenre(int genreId, int page) {
        return service.getMoviesByGenre(genreId, page);
    }

    static class GenresList {
        List<Genre> genres;
        public List<Genre> getGenres() {
            return genres;
        }
    }
}
