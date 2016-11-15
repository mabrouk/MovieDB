package com.mabrouk.moviedb.configurations;

import com.google.gson.annotations.SerializedName;
import com.mabrouk.moviedb.network.ApiInfo;

import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by VPN on 11/13/2016.
 */

public class ConfigurationServiceProvider {
    interface ConfigurationService {
        @GET("configuration?api_key=" + ApiInfo.API_KEY)
        Observable<ServiceResults> getConfigurations();
    }

    static ConfigurationService service = new Retrofit.Builder().baseUrl(ApiInfo.BASE_URL)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ConfigurationService.class);

    public static void syncConfigurationsIfNeeded() {
        if (needsUpdate()) {
            service.getConfigurations()
                    .retryWhen(new ExponentialBackoffRetryFunction())
                    .map(ServiceResults::mapToImages)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(ConfigurationsStore::updateConfigurations);
        }
    }

    /**
     * Determines if a configurations update needed
     * @return If a day has passed since last update it will return true returns false otherwise
     */
    private static boolean needsUpdate() {
        long lastUpdateTime = ConfigurationsStore.getLastUpdatedTime();
        long now = System.currentTimeMillis();
        long dayDiff = 1000 * 60 * 60 * 24;
        return (now - lastUpdateTime) > dayDiff;
    }

    static class ServiceResults {
        Images images;

        static Images mapToImages(ServiceResults serviceResults) {
            return serviceResults.images;
        }
    }

    static class Images {
        @SerializedName("secure_base_url")
        String baseUrl;

        @SerializedName("backdrop_sizes")
        List<String> backdropSizes;

        @SerializedName("poster_sizes")
        List<String> posterSizes;

        @SerializedName("profile_sizes")
        List<String> profileSizes;
    }

    static class ExponentialBackoffRetryFunction implements Func1<Observable<? extends Throwable>, Observable<?>> {
        int time = 1;

        @Override
        public Observable<?> call(Observable<? extends Throwable> observable) {
            return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                @Override
                public Observable<?> call(Throwable throwable) {
                    int delay = time;
                    time *= 2;
                    return Observable.timer(delay, TimeUnit.SECONDS);
                }
            });
        }
    }
}
