package com.mabrouk.moviedb.movie;

import com.google.gson.annotations.SerializedName;
import com.mabrouk.moviedb.common.BaseModel;
import com.mabrouk.moviedb.network.ApiInfo;

/**
 * Created by VPN on 11/1/2016.
 */

public class Movie extends BaseModel{
    String title;
    @SerializedName("poster_path")
    String posterPath;

    String overview;
    @SerializedName("release_date")
    String releaseDate;

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getThumbnailUrl() {
        return ApiInfo.IMAGES_BASE_URL + ApiInfo.POSTER_SIZE_THUMBNAIL + posterPath;
    }

}
