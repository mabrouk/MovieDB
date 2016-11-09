package com.mabrouk.moviedb.people;

import com.google.gson.annotations.SerializedName;
import com.mabrouk.moviedb.common.BaseModel;
import com.mabrouk.moviedb.common.DateUtils;
import com.mabrouk.moviedb.network.ApiInfo;
import com.mabrouk.moviedb.people.details.ExternalIds;
import com.mabrouk.moviedb.people.details.PersonCredit;
import com.mabrouk.moviedb.people.details.PersonResult;
import com.mabrouk.moviedb.people.details.ProfileImage;

import java.util.ArrayList;
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

    String imbdId;
    String homepage;


    @SerializedName("external_ids")
    ExternalIds externalIds;

    List<ProfileImage> profileImages;
    List<PersonCredit> creditList;

    public String getThumbnail() {
        return ApiInfo.IMAGES_BASE_URL + ApiInfo.PROFILE_SIZE_LARGE + profilePath;
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

    public String getImbdId() {
        return imbdId;
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
        }
    }

}
