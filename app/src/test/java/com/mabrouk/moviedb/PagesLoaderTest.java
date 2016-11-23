package com.mabrouk.moviedb;

import com.mabrouk.moviedb.common.BaseModel;
import com.mabrouk.moviedb.common.PagesLoader;
import com.mabrouk.moviedb.common.ResultList;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.plugins.RxJavaHooks;
import rx.schedulers.Schedulers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by VPN on 11/23/2016.
 */

public class PagesLoaderTest {
    ResultList<BaseModel> emptyResultList;
    ResultList<BaseModel> twoPagesResult;
    ResultList<BaseModel> onePagesResult;

    ResultList<BaseModel> errorResult;

    class LoadedListener implements PagesLoader.PageLoadedListener<BaseModel> {
        List<BaseModel> page;
        boolean hasError;

        @Override
        public void pageLoaded(List<BaseModel> page) {
            this.page = page;
            hasError = false;
        }

        @Override
        public void pageLoadFailed() {
            page = null;
            hasError = true;
        }
    }

    LoadedListener loadedListener = new LoadedListener();

    @Before
    public void setup() {
        //mocks
        emptyResultList = mock(ResultList.class);
        when(emptyResultList.getResults()).thenReturn(new ArrayList<>());
        when(emptyResultList.getTotalPages()).thenReturn(1);

        twoPagesResult = mock(ResultList.class);
        when(twoPagesResult.getResults()).thenReturn(Arrays.asList(new BaseModel(), new BaseModel(), new BaseModel()));
        when(twoPagesResult.getTotalPages()).thenReturn(2);

        onePagesResult = mock(ResultList.class);
        when(onePagesResult.getResults()).thenReturn(Arrays.asList(new BaseModel(), new BaseModel()));
        when(onePagesResult.getTotalPages()).thenReturn(2);

        errorResult = mock(ResultList.class);
        when(errorResult.getResults()).thenThrow(new RuntimeException("Just a runtime exception"));
        when(errorResult.getTotalPages()).thenThrow(new RuntimeException("Just a runtime exception"));

        //Rx hooks to override schedulers
        RxJavaHooks.setOnIOScheduler(scheduler -> Schedulers.immediate());
        RxAndroidPlugins.getInstance().reset();
        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook(){
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });
    }

    @Test
    public void testEmptyResultList() {
        PagesLoader<BaseModel> pagesLoader = new PagesLoader<>(page -> Observable.just(emptyResultList));
        assertThat(pagesLoader.hasMorePages()).isEqualTo(true);
        pagesLoader.listenForPageLoaded(loadedListener);
        assertThat(loadedListener.page).as("Empty result should be empty page").isEmpty();
        assertThat(loadedListener.hasError).as("Empty result shouldn't have an error").isEqualTo(false);
        assertThat(pagesLoader.hasMorePages()).isEqualTo(false);
    }

    @Test
    public void testTwoResultsList() {
        PagesLoader<BaseModel> pagesLoader = new PagesLoader<>(page -> {
            if(page == 1) {
                return Observable.just(twoPagesResult);
            }else{
                return Observable.just(onePagesResult);
            }
        });

        pagesLoader.listenForPageLoaded(loadedListener);
        assertThat(loadedListener.page).size().as("Check that first page got three elements").isEqualTo(3);
        assertThat(pagesLoader.hasMorePages()).as("Check that after two pages has one more page").isEqualTo(true);

        pagesLoader.loadNextPage();
        assertThat(loadedListener.page).size().as("Check that second page has size of 2").isEqualTo(2);
        assertThat(pagesLoader.hasMorePages()).isEqualTo(false);
    }

    @Test
    public void testError() {
        final Queue<ResultList<BaseModel>> errorQueue = new ArrayDeque<>();
        errorQueue.add(errorResult);
        errorQueue.add(onePagesResult);

        PagesLoader<BaseModel> pagesLoader = new PagesLoader<>(page -> {
            ResultList<BaseModel> t = errorQueue.poll();
            return Observable.just(t);
        });

        pagesLoader.listenForPageLoaded(loadedListener);
        assertThat(loadedListener.hasError).as("Got error").isEqualTo(true);
        pagesLoader.loadNextPage();
        assertThat(loadedListener.hasError).as("No error on retry").isEqualTo(false);
        assertThat(loadedListener.page).size().as("Got result on retry").isEqualTo(2);
    }
}
