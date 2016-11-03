package com.mabrouk.moviedb.people;

import com.google.gson.annotations.SerializedName;
import com.mabrouk.moviedb.network.ApiInfo;

/**
 * Created by VPN on 11/2/2016.
 */

public class Person {
    int id;
    String name;
    @SerializedName("profile_path")
    String profilePath;

    public String getThumbnail() {
        return ApiInfo.IMAGES_BASE_URL + ApiInfo.POSTER_SIZE_THUMBNAIL + profilePath;
    }
}
