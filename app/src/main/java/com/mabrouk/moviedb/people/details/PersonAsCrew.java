package com.mabrouk.moviedb.people.details;

import com.google.gson.annotations.SerializedName;
import com.mabrouk.moviedb.common.BaseModel;

/**
 * Created by User on 11/9/2016.
 */

public class PersonAsCrew extends PersonCredit {
    String job;
    String department;

    public String getDepartment() {
        return department;
    }

    @Override
    public String getRole() {
        return job;
    }
}
