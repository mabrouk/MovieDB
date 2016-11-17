package com.mabrouk.moviedb.tv.season;

import com.google.gson.annotations.SerializedName;
import com.mabrouk.moviedb.common.BaseModel;
import com.mabrouk.moviedb.network.ApiInfo;

/**
 * Created by User on 11/12/2016.
 */

public class Season extends BaseModel {
    @SerializedName("season_number")
    int seasonNumber;
    @SerializedName("episode_count")
    int episodeCount;
    @SerializedName("poster_path")
    String posterPath;

    @SerializedName("air_date")
    String airDate;

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public int getEpisodeCount() {
        return episodeCount;
    }

    public String getPosterLargeUrl() {
        return ApiInfo.IMAGES_BASE_URL + ApiInfo.POSTER_SIZE_LARGE + posterPath;
    }

    public String getPosterThum() {
        return ApiInfo.IMAGES_BASE_URL + ApiInfo.POSTER_SIZE_LARGE_THUMBNAIL + posterPath;
    }
}
