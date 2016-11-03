package com.mabrouk.moviedb.common;

import java.util.List;

/**
 * Created by VPN on 11/3/2016.
 */

public class ResultList <T extends BaseModel> {
    int page;
    List<T> results;

    public List<T> getResults() {
        return results;
    }
}
