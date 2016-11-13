package com.mabrouk.moviedb.configurations;

import java.util.Arrays;
import java.util.List;

/**
 * Created by VPN on 11/13/2016.
 */

public class ConfigurationDefaults {
    public final static String IMAGES_DEFAULT_BASE_URL = "https://image.tmdb.org/t/p/";
    public final static String[] POSTER_DEFAULT_CONFIGURATIONS = new String[]{
            "w92",
            "w154",
            "w185",
            "w342",
            "w500",
            "w780",
            "original"
    };

    public final static String[] BACKDROP_DEFAULT_CONFIGURATIONS = new String[] {
            "w300",
            "w780",
            "w1280",
            "original"
    };

    public final static String[] PROFILE_DEFAULT_CONFIGURATIONS = new String[] {
            "w45",
            "w185",
            "h632",
            "original"
    };
}
