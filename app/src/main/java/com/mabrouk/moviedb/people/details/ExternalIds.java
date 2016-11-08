package com.mabrouk.moviedb.people.details;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 11/7/2016.
 */

public class ExternalIds {
    @SerializedName("facebook_id")
    String facebookId;
    @SerializedName("instagarm_id")
    String instagramId;
    @SerializedName("twitter_id")
    String twitterId;
}
