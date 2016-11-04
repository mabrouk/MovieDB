package com.mabrouk.moviedb.movie.details;

import java.util.List;

/**
 * Created by VPN on 11/4/2016.
 */

public class MovieVideo {
    String type;
    String name;
    String site;

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    String key;

    public String getUrl() {
        return "https://www.youtube.com/watch?v=" + key;
    }

    public String getThumbnailUrl() {
        return String.format("https://i.ytimg.com/vi/%s/hqdefault.jpg", key);
    }

    public static class VideoList {
        List<MovieVideo> results;

        public List<MovieVideo> getResults() {
            return results;
        }
    }
}