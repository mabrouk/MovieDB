package com.mabrouk.moviedb.people.details;

import com.google.gson.annotations.SerializedName;
import com.mabrouk.moviedb.common.BaseModel;

/**
 * Created by User on 11/9/2016.
 */

public abstract class PersonCredit extends BaseModel {
    @SerializedName("media_type")
    String mediaType;

    @SerializedName("release_date")
    String releaseDate;

    String title;
    String name;

    public String getTitle() {
        return mediaType.equals("tv") ? name : title;
    }

    public String getMediaType() {
        return mediaType;
    }

    public abstract String getRole();

    public String getReleaseDate() {
        if(releaseDate == null)
            return "";
        return releaseDate;
    }

    public boolean isMovieCredit() {
        return mediaType.equals("movie");
    }
}
