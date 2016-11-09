package com.mabrouk.moviedb.people.details;

import com.google.gson.annotations.SerializedName;
import com.mabrouk.moviedb.people.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by User on 11/8/2016.
 */

public class PersonResult extends Person {
    Images images;

    @SerializedName("combined_credits")
    CombinedCredits combinedCredits;

    public List<ProfileImage> getProfileImages() {
        return images.profiles;
    }

    public ArrayList<PersonCredit> getCombinedCredits() {
        ArrayList<PersonCredit> list = new ArrayList<>(combinedCredits.cast.size() + combinedCredits.crew.size());

        Collections.sort(combinedCredits.cast, this::movieFirstCompare);
        list.addAll(combinedCredits.cast);

        Collections.sort(combinedCredits.crew, this::departmentFirstCompare);
        list.addAll(combinedCredits.crew);

        return list;
    }

    private int movieFirstCompare(PersonCredit p1, PersonCredit p2) {
        int typeCompare = p1.mediaType.compareTo(p2.mediaType);
        if(p1.releaseDate == null || p2.releaseDate == null)
            return typeCompare;

        if(typeCompare == 0 && p1.isMovieCredit())
            return p1.releaseDate.compareTo(p2.releaseDate) * -1;
        return typeCompare;
    }

    private int departmentFirstCompare(PersonAsCrew p1, PersonAsCrew p2) {
        int departmentCompare = p1.department.compareTo(p2.department);
        if(departmentCompare == 0) {
            return movieFirstCompare(p1, p2);
        }
        return departmentCompare;
    }

    static class Images {
        List<ProfileImage> profiles;
    }

    static class CombinedCredits {
        List<PersonAsCast> cast;
        List<PersonAsCrew> crew;
    }
}
