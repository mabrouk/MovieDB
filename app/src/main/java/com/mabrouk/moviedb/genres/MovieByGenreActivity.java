package com.mabrouk.moviedb.genres;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.common.PagesLoader;
import com.mabrouk.moviedb.common.ResultList;
import com.mabrouk.moviedb.movie.Movie;
import com.mabrouk.moviedb.movie.MovieListFragment;

import rx.Observable;

public class MovieByGenreActivity extends AppCompatActivity {
    private static final String EXTRA_GENRE_ID = "id";
    private static final String EXTRA_GENRE_NAME = "name";

    public static void startInstance(Activity activity, Genre genre) {
        Intent intent = new Intent(activity, MovieByGenreActivity.class);
        intent.putExtra(EXTRA_GENRE_NAME, genre.getName());
        intent.putExtra(EXTRA_GENRE_ID, genre.getId());
        activity.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_by_genre);
        int id = getIntent().getIntExtra(EXTRA_GENRE_ID, 0);
        String name = getIntent().getStringExtra(EXTRA_GENRE_NAME);
        setTitle(name + " Movies");

        MovieListFragment movieListFragment = new MovieListFragment();
        movieListFragment.setPagesLoader(new PagesLoader<Movie>(page -> GenresServiceProvider.getMoviesByGenre(id, page)));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.root_layout, movieListFragment)
                .commit();
    }
}
