package com.mabrouk.moviedb.people;

import com.google.gson.annotations.SerializedName;
import com.mabrouk.moviedb.common.BaseModel;
import com.mabrouk.moviedb.common.DateUtils;
import com.mabrouk.moviedb.network.ApiInfo;

/**
 * Created by VPN on 11/2/2016.
 */

public class Person extends BaseModel {
    String name;
    @SerializedName("profile_path")
    String profilePath;

    @SerializedName("birthday")
    String birthDate;
    @SerializedName("deathday")
    String deathDate;
    String biography;

    String imbdId;
    String homepage;

    public String getThumbnail() {
        return ApiInfo.IMAGES_BASE_URL + ApiInfo.PROFILE_SIZE_LARGE + profilePath;
    }

    public String getName() {
        return name;
    }

    public String getBirthDate() {
        return DateUtils.formatDateString(birthDate);
    }

    public String getDeathDate() {
        return DateUtils.formatDateString(deathDate);
    }

    public String getBiography() {
        return biography;
    }

    public String getImbdId() {
        return imbdId;
    }

    public String getHomepage() {
        return homepage;
    }

    public void populateFrom(Person other) {
        biography = other.biography;
        birthDate = other.birthDate;
        deathDate = other.deathDate;
        homepage = other.homepage;
        imbdId = other.imbdId;
    }


}
