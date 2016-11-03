package com.mabrouk.moviedb.common;

import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by VPN on 11/2/2016.
 */

public class PagesLoader<RD extends BaseModel> {
    int page = 1;
    PageLoadingOperation<ResultList<RD>> operation;
    PageLoadedListener<RD> listener;
    Subscription subscription;

    //since calling loadingNextPage() and observing are both done on main thread, no need to be atomic
    boolean loading;

    public PagesLoader(PageLoadingOperation<ResultList<RD>> operation) {
        this.operation = operation;
    }

    public interface PageLoadingOperation<T> {
        Observable<T> loadPage(int page);
    }

    public interface PageLoadedListener<V> {
        void pageLoaded(List<V> page);
        void pageLoadFailed();
    }

    public void loadNextPage() {
        if(loading)
            return;
        loading = true;

        subscription = operation.loadPage(page)
                .map(ResultList::getResults)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::loadedPage, this::gotError);
    }

//    protected abstract List<RD> map(LD dataList);

    public void listenForPageLoaded(PageLoadedListener listener) {
        this.listener = listener;
        loadNextPage();
    }

    public void stopListeningForPageLoaded(PageLoadedListener listener) {
        this.listener = null;
        if(subscription != null)
            subscription.unsubscribe();
        subscription = null;
    }

    protected void loadedPage(List<RD> rawDataList) {
        if(listener != null)
            listener.pageLoaded(rawDataList);
        page++;
        loading = false;
    }

    protected void gotError(Throwable error) {
        if(listener != null)
            listener.pageLoadFailed();
        loading = false;
    }
}
