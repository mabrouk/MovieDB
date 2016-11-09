package com.mabrouk.moviedb.people.details;

import com.google.gson.annotations.SerializedName;
import com.mabrouk.moviedb.network.ApiInfo;

/**
 * Created by User on 11/8/2016.
 */

public class ProfileImage {
    @SerializedName("file_path")
    String filePath;

    int width;
    @SerializedName("aspect_ratio")
    double aspectRatio;

    public String getThumbnailUrl() {
        return ApiInfo.IMAGES_BASE_URL + ApiInfo.PROFILE_SIZE_LARGE + filePath;
    }
}
