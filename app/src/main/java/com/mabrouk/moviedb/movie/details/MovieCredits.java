package com.mabrouk.moviedb.movie.details;

import com.mabrouk.moviedb.people.Cast;
import com.mabrouk.moviedb.people.Crew;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VPN on 11/4/2016.
 */

public class MovieCredits {
    List<Cast> cast;
    List<Crew> crew;

    public static MovieCredits filter(MovieCredits other) {
        MovieCredits credits = new MovieCredits();
        credits.crew = new ArrayList<>();
        credits.cast = new ArrayList<>();

        for(int i = 0; i < other.crew.size(); i++) {
            if(other.crew.get(i).getJob().equals("Director"))
                credits.crew.add(other.crew.get(i));
        }

        for(int i = 0; i < other.cast.size() && i < 12; i++) {
            credits.cast.add(other.cast.get(i));
        }

        return credits;
    }
}
