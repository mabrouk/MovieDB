package com.mabrouk.moviedb.people;

import com.google.gson.annotations.SerializedName;
import com.mabrouk.moviedb.common.BaseModel;
import com.mabrouk.moviedb.network.ApiInfo;

/**
 * Created by VPN on 11/4/2016.
 */

public class Cast extends BaseModel {
    String name;
    String character;
    @SerializedName("profile_path")
    String profilePath;

    public String getName() {
        return name;
    }

    public String getCharacter() {
        return character;
    }

    public String getProfileUrl() {
        return ApiInfo.IMAGES_BASE_URL + ApiInfo.PROFILE_SIZE_LARGE + profilePath;
    }
}
