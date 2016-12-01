package com.mabrouk.moviedb.tv;

import com.google.gson.annotations.SerializedName;
import com.mabrouk.moviedb.common.BaseModel;
import com.mabrouk.moviedb.common.Utils.DateUtils;
import com.mabrouk.moviedb.genres.Genre;
import com.mabrouk.moviedb.movie.details.MovieCredits;
import com.mabrouk.moviedb.people.Person;
import com.mabrouk.moviedb.tv.season.Season;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by VPN on 11/3/2016.
 */

public class Tv extends BaseModel {
    String name;
    @SerializedName("poster_path")
    String posterPath;
    String overview;
    @SerializedName("backdrop_path")
    String backdropPath;

    @SerializedName("vote_average")
    double rating;
    @SerializedName("first_air_date")
    String firstAirDate;

    List<Genre> genres;
    String homepage;
    String status;
    List<Season> seasons;

    @SerializedName("created_by")
    List<Person> createdBy;

    MovieCredits credits;

    public double getRating() {
        return rating;
    }

    public String getFirstAirDateFormatted() {
        return DateUtils.formatDateString(firstAirDate);
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public String getHomepage() {
        return homepage;
    }

    public String getStatus() {
        return status;
    }

    public List<Person> getCreatedBy() {
        return createdBy;
    }

    public String getName() {
        return name;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public List<Season> getSeasons() {
        return seasons;
    }

    public String getOverview() {
        return overview;
    }

    public String getDisplayableRating() {
        return String.format(Locale.US, "%.1f", rating);
    }

    public MovieCredits getCredits() {
        return credits;
    }


    public void populateFrom(Tv other) {
        this.genres = other.genres;
        this.homepage = other.homepage;
        this.status = other.status;
        this.createdBy = other.createdBy;

        this.credits = other.credits;

        //remove season 0
        if(other.seasons.get(0).getSeasonNumber() == 0)
            other.seasons.remove(0);
        this.seasons = other.seasons;

        //sorting desc
        Collections.reverse(this.seasons);
    }
}
