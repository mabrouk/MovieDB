package com.mabrouk.moviedb.tv.season;

import com.google.gson.annotations.SerializedName;
import com.mabrouk.moviedb.common.BaseModel;
import com.mabrouk.moviedb.movie.details.Cast;
import com.mabrouk.moviedb.network.ApiInfo;

import java.util.List;

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

    String name;
    String overview;

    List<Episode> episodes;

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public int getEpisodeCount() {
        return episodeCount;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getName() {
        return name;
    }

    public String getOverview() {
        return overview;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void populateFrom(Season other) {
        this.episodes = other.episodes;
        this.overview = other.overview;
    }
}
