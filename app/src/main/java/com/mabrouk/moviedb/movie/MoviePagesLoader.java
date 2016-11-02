package com.mabrouk.moviedb.movie;

import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by VPN on 11/2/2016.
 */

public class MoviePagesLoader {
    public interface PageLoadingOperation {
        Observable<MovieList> loadPage(int page);
    }

    public interface PageLoadedListener {
        void pageLoaded(List<Movie> page);
        void pageLoadFailed();
    }

    int page = 1;
    PageLoadingOperation operation;
    PageLoadedListener listener;
    Subscription subscription;

    //since calling loadingNextPage() and observing are both done on main thread, no need to be atomic
    boolean loading;

    public MoviePagesLoader(PageLoadingOperation operation) {
        this.operation = operation;
    }

    public void listenForPageLoaded(PageLoadedListener listener) {
        this.listener = listener;
        loadNextPage();
    }

    public void stopListeningForPageLoaded(PageLoadedListener listener) {
        this.listener = null;
        subscription.unsubscribe();
        subscription = null;
    }

    public void loadNextPage() {
        if(loading)
            return;
        loading = true;

        subscription = operation.loadPage(page)
                .map(movieList -> movieList.getMovies())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::loadedPage, this::gotError);
    }

    private void loadedPage(List<Movie> movies) {
        if(listener != null)
            listener.pageLoaded(movies);
        page++;
        loading = false;
    }

    private void gotError(Throwable error) {
        if(listener != null)
            listener.pageLoadFailed();
        loading = false;
    }
}
