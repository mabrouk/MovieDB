package com.mabrouk.moviedb.configurations;

import com.google.gson.annotations.SerializedName;
import com.mabrouk.moviedb.network.ApiInfo;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
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

    public static Observable<Images> getConfigurations() {
        return service.getConfigurations()
                .map(ServiceResults::mapToImages)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
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
}
