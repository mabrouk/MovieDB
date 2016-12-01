package com.mabrouk.moviedb.common.Utils;

/**
 * Created by VPN on 11/5/2016.
 */

public class ExternalUrlUtil {
    private static final String IMDB_BASE_URL = "http://m.imdb.com/title/";
    private static final String IMDB_PERSON_BASE_URL = "http://m.imdb.com/name/";
    private static final String YOUTUBE_BASE_URL = "https://m.youtube.com/watch?v=";

    public static String IMDBUrlForTitle(String title){
        return IMDB_BASE_URL + title;
    }

    public static String IMDBUrlForPerson(String name){
        return IMDB_PERSON_BASE_URL+ name;
    }

    public static String youtubeUrlForKey(String key) {
        return YOUTUBE_BASE_URL + key;
    }
}
