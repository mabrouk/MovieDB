package com.mabrouk.moviedb.common;

import org.junit.Before;
import org.junit.BeforeClass;
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
import static org.mockito.Mockito.verify;
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

    LoadedListener loadedListener;

    @BeforeClass
    public static void staticSetup() {
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

    @Before
    public void setup() {
        loadedListener = new LoadedListener();

        emptyResultList = createResultList(new ArrayList<>(), 1);
        twoPagesResult = createResultList(Arrays.asList(new BaseModel(), new BaseModel(), new BaseModel()), 2);
        onePagesResult = createResultList(Arrays.asList(new BaseModel(), new BaseModel()), 2);

        errorResult = mock(ResultList.class);
        when(errorResult.getResults()).thenThrow(new RuntimeException("Just a runtime exception"));
        when(errorResult.getTotalPages()).thenThrow(new RuntimeException("Just a runtime exception"));
    }

    @Test
    public void newPagesLoaderHasPagesToGetTest() {
        PagesLoader<BaseModel> pagesLoader = new PagesLoader<>(page -> Observable.just(emptyResultList));
        assertThat(pagesLoader.hasMorePages()).isEqualTo(true);
    }

    @Test
    public void testNullListenerDoesntTriggerLoading() {
        PagesLoader<BaseModel> pagesLoader = new PagesLoader<>(page -> Observable.just(onePagesResult));
        pagesLoader.listenForPageLoaded(null);
        assertThat(pagesLoader.hasMorePages()).isTrue();
    }

    @Test
    public void testPagesLoaderLoadsEmptyList() {
        PagesLoader<BaseModel> pagesLoader = new PagesLoader<>(page -> Observable.just(emptyResultList));
        pagesLoader.listenForPageLoaded(loadedListener);

        assertThat(loadedListener.page).as("Empty result should be empty page").isEmpty();
        assertThat(pagesLoader.hasMorePages()).as("Empty result doesn't have more pages").isEqualTo(false);
    }

    @Test
    public void testFailureInPageLoading() {
        PagesLoader<BaseModel> pagesLoader = new PagesLoader<>(page -> Observable.just(errorResult));
        pagesLoader.listenForPageLoaded(loadedListener);

        assertThat(loadedListener.hasError).as("Failed to load the page should signal error").isEqualTo(true);
        assertThat(pagesLoader.hasMorePages()).as("Failure in loading page doesn't change ability to load more pages").isEqualTo(true);
    }

    @Test
    public void testLoadingPage() {
        PagesLoader<BaseModel> pagesLoader = new PagesLoader<>(page -> Observable.just(onePagesResult));
        pagesLoader.listenForPageLoaded(loadedListener);

        assertThat(loadedListener.page).size().as("Check that loaded page has three elements").isEqualTo(2);
    }

    @Test
    public void testLoadingAnotherPage() {
        PagesLoader<BaseModel> pagesLoader = new PagesLoader<>(page -> {
            if(page == 1) {
                return Observable.just(twoPagesResult);
            }else{
                return Observable.just(onePagesResult);
            }
        });
        pagesLoader.listenForPageLoaded(loadedListener);
        pagesLoader.loadNextPage();

        assertThat(loadedListener.page).size().as("Check that second page has size of 2").isEqualTo(2);
        assertThat(pagesLoader.hasMorePages()).as("Check that after loading second page there are no more pages").isEqualTo(false);
    }

    @Test
    public void testLoadPageSuccessfullyAfterFailureAttempt() {
        final Queue<ResultList<BaseModel>> errorQueue = new ArrayDeque<>();
        errorQueue.add(errorResult);
        errorQueue.add(onePagesResult);

        PagesLoader<BaseModel> pagesLoader = new PagesLoader<>(page -> {
            ResultList<BaseModel> t = errorQueue.poll();
            return Observable.just(t);
        });

        pagesLoader.listenForPageLoaded(loadedListener);
        pagesLoader.loadNextPage();

        assertThat(loadedListener.hasError).as("No error on retry").isEqualTo(false);
        assertThat(loadedListener.page).size().as("Got result on retry").isEqualTo(2);
    }


    //util

    ResultList<BaseModel> createResultList(List<BaseModel> list, int totalPages) {
        ResultList<BaseModel> resultList = new ResultList<>();
        resultList.totalPages = totalPages;
        resultList.results = list;
        return resultList;
    }
}
