package com.mabrouk.moviedb.tv;

import com.google.gson.annotations.SerializedName;
import com.mabrouk.moviedb.common.BaseModel;
import com.mabrouk.moviedb.network.ApiInfo;

import java.util.Locale;

/**
 * Created by VPN on 11/3/2016.
 */

public class Tv extends BaseModel{
    String name;
    @SerializedName("poster_path")
    String posterPath;
    String overview;

    @SerializedName("vote_average")
    double rating;

    public String getName() {
        return name;
    }

    public String getThumbnailUrl() {
        return ApiInfo.IMAGES_BASE_URL + ApiInfo.POSTER_SIZE_THUMBNAIL + posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getDisplayableRating() {
        return String.format(Locale.US, "%.1f/10", rating);
    }
}
