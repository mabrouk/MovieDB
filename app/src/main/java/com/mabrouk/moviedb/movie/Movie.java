package com.mabrouk.moviedb.movie;

import com.google.gson.annotations.SerializedName;
import com.mabrouk.moviedb.common.BaseModel;
import com.mabrouk.moviedb.common.DateUtils;
import com.mabrouk.moviedb.genres.Genre;
import com.mabrouk.moviedb.network.ApiInfo;

import java.util.List;
import java.util.Locale;

/**
 * Created by VPN on 11/1/2016.
 */

public class Movie extends BaseModel {
    String title;
    @SerializedName("poster_path")
    String posterPath;

    String overview;
    @SerializedName("release_date")
    String releaseDate;
    @SerializedName("backdrop_path")
    String backdropPath;
    @SerializedName("imdb_id")
    String imdb;
    @SerializedName("homepage")
    String website;
    @SerializedName("vote_average")
    double rating;
    int revenue;
    int budget;
    int runtime;
    List<Genre> genres;
    String status;

    public String getImdb() {
        return imdb;
    }

    public String getWebsite() {
        return website;
    }

    public double getRating() {
        return rating;
    }

    public int getRevenue() {
        return revenue;
    }

    public int getBudget() {
        return budget;
    }

    public int getRuntime() {
        return runtime;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public String getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getFormattedReleaseDate() {
        return DateUtils.formatDateString(releaseDate);
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getDisplayableRating() {
        return String.format(Locale.US, "%.1f", rating);
    }

    public void populateFrom(Movie other) {
        this.genres = other.genres;
        this.revenue = other.revenue;
        this.budget = other.budget;
        this.status = other.status;
        this.imdb = other.imdb;
        this.website = other.website;
        this.runtime = other.runtime;
    }
}
