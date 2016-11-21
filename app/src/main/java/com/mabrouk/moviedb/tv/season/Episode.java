package com.mabrouk.moviedb.tv.season;

import com.google.gson.annotations.SerializedName;
import com.mabrouk.moviedb.common.BaseModel;
import com.mabrouk.moviedb.common.DateUtils;

/**
 * Created by VPN on 11/21/2016.
 */

public class Episode extends BaseModel {
    @SerializedName("episode_number")
    int number;

    @SerializedName("air_date")
    String airDate;

    String name;
    String overview;

    public int getNumber() {
        return number;
    }

    public String getAirDateFormatted() {
        return DateUtils.formatDateString(airDate);
    }

    public String getName() {
        return name;
    }

    public String getOverview() {
        return overview;
    }
}
