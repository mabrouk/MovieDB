package com.mabrouk.moviedb.people.details;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 11/7/2016.
 */

public class ExternalIds {
    private final static String FACEBOOK_BASE_URL = "https://www.facebook.com/";
    private final static String INSTAGRAM_BASE_URL = "https://www.instagram.com/";
    private final static String TWITTER_BASE_URL = "https://twitter.com/";

    @SerializedName("facebook_id")
    String facebookId;
    @SerializedName("instagram_id")
    String instagramId;
    @SerializedName("twitter_id")
    String twitterId;

    public boolean hasFacebook() {
        return facebookId != null && facebookId.length() > 0;
    }

    public boolean hasTwitter() {
        return twitterId != null && twitterId.length() > 0;
    }

    public boolean hasInstagram() {
        return instagramId != null && instagramId.length() > 0;
    }

    public String getFacebookUrl() {
        return FACEBOOK_BASE_URL + facebookId;
    }

    public String getInstagramUrl() {
        return INSTAGRAM_BASE_URL + instagramId + "/";
    }

    public String getTwitterUrl() {
        return TWITTER_BASE_URL + twitterId;
    }
}
