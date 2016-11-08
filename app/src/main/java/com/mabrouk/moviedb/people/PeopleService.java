package com.mabrouk.moviedb.people;

import com.mabrouk.moviedb.common.ResultList;
import com.mabrouk.moviedb.network.ApiInfo;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by VPN on 11/2/2016.
 */

public interface PeopleService {
    @GET("person/popular?api_key=" + ApiInfo.API_KEY)
    Observable<ResultList<Person>> getPopularPeople(@Query("page") int page);

    @GET("person/{person_id}?api_key=" + ApiInfo.API_KEY)
    Observable<Person> getPersonDetails(@Path("person_id") int personId);
}
