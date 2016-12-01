package com.mabrouk.moviedb.movie.details;

import com.google.gson.annotations.SerializedName;
import com.mabrouk.moviedb.common.BaseModel;
import com.mabrouk.moviedb.network.ApiInfo;
import com.mabrouk.moviedb.people.Person;

/**
 * Created by VPN on 11/4/2016.
 */

public class Cast extends Person {
    String character;

    public String getCharacter() {
        return character;
    }
}
