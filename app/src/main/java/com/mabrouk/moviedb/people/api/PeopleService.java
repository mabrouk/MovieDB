package com.mabrouk.moviedb.people.api;

import com.mabrouk.moviedb.common.ResultList;
import com.mabrouk.moviedb.network.ApiInfo;
import com.mabrouk.moviedb.people.Person;
import com.mabrouk.moviedb.people.details.PersonResult;

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

    @GET("search/person?api_key=" + ApiInfo.API_KEY)
    Observable<ResultList<Person>> search(@Query("query") String query, @Query("page") int page);

    @GET("person/{person_id}?api_key=" + ApiInfo.API_KEY +
            "&append_to_response=images,external_ids,combined_credits")
    Observable<PersonResult> getPersonDetails(@Path("person_id") int personId);
}
