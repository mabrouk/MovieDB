package com.mabrouk.moviedb.movie;

import com.mabrouk.moviedb.common.PagesLoader;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by VPN on 11/2/2016.
 */

public class MoviePagesLoader extends PagesLoader<Movie, MovieList> {

    public MoviePagesLoader(PageLoadingOperation<MovieList> operation) {
        super(operation);
    }

    @Override
    protected List<Movie> map(MovieList dataList) {
        return dataList.getMovies();
    }
}
