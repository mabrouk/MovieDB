package com.mabrouk.moviedb.common;

import android.content.Intent;
import android.util.Pair;

import com.mabrouk.moviedb.movie.Movie;
import com.mabrouk.moviedb.people.Person;

import java.util.HashMap;

/**
 * Created by VPN on 11/3/2016.
 */

/**
 * class used to transfer data objects between activities and other android classes
 */
public class DataBag {
    static Pocket<Movie> moviesPocket = new Pocket<>();
    static Pocket<Person> peoplePocket = new Pocket<>();


    public static void addMovieToPocket(Movie movie) {
        moviesPocket.addObject(movie);
    }

    public static Movie getMovieFromPocket(int movieId) {
        return moviesPocket.getObject(movieId);
    }

    public static void removeMovieFromPocket(int movieId) {
        moviesPocket.removeObject(movieId);
    }

    public static void addPersonToPocket(Person person) {
        peoplePocket.addObject(person);
    }

    public static Person getPersonFromPocket(int personId) {
        return peoplePocket.getObject(personId);
    }

    public static void removePersonFromPocket(int personId) {
        peoplePocket.removeObject(personId);
    }


    // No synchronization needed, as all the calls will be made from UI thread
    static class Pocket<T extends BaseModel> {
        HashMap<Integer, Pair<Integer, T>> pocketMap = new HashMap<>();

        public void addObject(T object) {
            Pair<Integer, T> pair = pocketMap.get(object.getId());
            if(pair == null)
                pair = new Pair<>(1, object);
            else
                pair = new Pair<>(pair.first + 1, pair.second);
            pocketMap.put(object.getId(), pair);
        }

        public T getObject(int movieId) {
            Pair<Integer, T> pair = pocketMap.get(movieId);
            if (pair == null)
                return null;
            return pair.second;
        }

        public void removeObject(int movieId) {
            Pair<Integer, T> pair = pocketMap.get(movieId);
            if (pair == null)
                return;

            if (pair.first == 1)
                pocketMap.remove(movieId);
            else
                pocketMap.put(movieId, new Pair<>(pair.first -1, pair.second));
        }
    }
}
