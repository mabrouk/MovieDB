package com.mabrouk.moviedb.common;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by VPN on 11/3/2016.
 */

public class ResultList <T extends BaseModel> {
    int page;
    List<T> results;
    @SerializedName("total_pages")
    int totalPages;

    public List<T> getResults() {
        return results;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
