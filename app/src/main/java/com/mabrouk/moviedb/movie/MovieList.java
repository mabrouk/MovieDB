package com.mabrouk.moviedb.movie;

import java.util.List;

/**
 * Created by VPN on 11/1/2016.
 */

public class MovieList {
    int page;
    List<Movie> results;

    public List<Movie> getMovies() {
        return results;
    }
}
