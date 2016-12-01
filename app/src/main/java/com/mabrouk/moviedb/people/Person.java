package com.mabrouk.moviedb.people;

import com.google.gson.annotations.SerializedName;
import com.mabrouk.moviedb.common.BaseModel;
import com.mabrouk.moviedb.common.Utils.DateUtils;
import com.mabrouk.moviedb.common.Utils.ExternalUrlUtil;
import com.mabrouk.moviedb.movie.Movie;
import com.mabrouk.moviedb.people.details.ExternalIds;
import com.mabrouk.moviedb.people.details.PersonCredit;
import com.mabrouk.moviedb.people.details.PersonResult;
import com.mabrouk.moviedb.people.details.ProfileImage;

import java.util.List;

/**
 * Created by VPN on 11/2/2016.
 */

public class Person extends BaseModel {
    String name;
    @SerializedName("profile_path")
    String profilePath;

    @SerializedName("birthday")
    String birthDate;
    @SerializedName("deathday")
    String deathDate;
    String biography;

    @SerializedName("imdb_id")
    String imbdId;
    String homepage;

    @SerializedName("known_for")
    List<Movie> knownFor;

    @SerializedName("external_ids")
    ExternalIds externalIds;

    List<ProfileImage> profileImages;
    List<PersonCredit> creditList;

    public boolean hasHomepage() {
        return homepage != null && homepage.length() > 0;
    }

    public boolean hasImdb() {
        return imbdId != null && imbdId.length() > 0;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public String getName() {
        return name;
    }

    public String getBirthDate() {
        return DateUtils.formatDateString(birthDate);
    }

    public String getDeathDate() {
        return DateUtils.formatDateString(deathDate);
    }

    public String getBiography() {
        return biography;
    }

    public String getImbdUrl() {
        return ExternalUrlUtil.IMDBUrlForPerson(imbdId);
    }

    public String getHomepage() {
        return homepage;
    }

    public ExternalIds getExternalIds() {
        return externalIds;
    }

    public List<ProfileImage> getProfileImages() {
        return profileImages;
    }

    public List<PersonCredit> getCreditList() {
        return creditList;
    }

    public List<Movie> getKnownFor() {
        return knownFor;
    }

    public void populateFrom(Person other) {
        biography = other.biography;
        birthDate = other.birthDate;
        deathDate = other.deathDate;
        homepage = other.homepage;
        imbdId = other.imbdId;
        if(other instanceof PersonResult) {
            PersonResult result = (PersonResult) other;
            this.profileImages = result.getProfileImages();
            this.creditList = result.getCombinedCredits();
            this.externalIds = result.getExternalIds();
        }
    }

}
