package com.mabrouk.moviedb.movie.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.common.DataBag;
import com.mabrouk.moviedb.common.GenresLayout;
import com.mabrouk.moviedb.common.RatingUtils;
import com.mabrouk.moviedb.movie.Movie;
import com.mabrouk.moviedb.movie.api.MovieServiceProvider;
import com.mabrouk.moviedb.network.MediaUrlBuilder;
import com.squareup.picasso.Picasso;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MovieDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE_ID = "movie_id";
    public static final String EXTRA_MOVIE_TITLE = "movie_title";

    public static void startMovieDetailsActivity(Context context, Movie movie) {
        DataBag.addMovieToPocket(movie);
        startMovieDetailsActivity(context, movie.getId(), movie.getTitle());
    }

    public static void startMovieDetailsActivity(Context context, int movieId, String title) {
        Intent intent = new Intent(context, MovieDetailsActivity.class);
        intent.putExtra(MovieDetailsActivity.EXTRA_MOVIE_ID, movieId);
        intent.putExtra(EXTRA_MOVIE_TITLE, title);
        context.startActivity(intent);
    }

    private int movieId;
    private Movie movie;

    Subscription subscription;
    GenresLayout genresLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        movieId = getIntent().getIntExtra(EXTRA_MOVIE_ID, 0);
        if (movieId == 0)
            throw new IllegalStateException("MovieDetailsActivity intent must contain movie id");

        String movieTitle = getIntent().getStringExtra(EXTRA_MOVIE_TITLE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(movieTitle);

        movie = DataBag.getMovieFromPocket(movieId);
        if (movie != null) {
            setBasicUI(movie);
        }

        genresLayout = (GenresLayout) findViewById(R.id.genres_layout);

        subscribeToService();
    }

    private void setBasicUI(Movie movie) {
        ImageView backdropImageView = (ImageView) findViewById(R.id.backdrop);
        TextView rating = (TextView) findViewById(R.id.rating);

        String backdropUrl = new MediaUrlBuilder(movie.getBackdropPath())
                .addType(MediaUrlBuilder.TYPE_POSTER)
                .build();

        Picasso.with(this).load(backdropUrl).into(backdropImageView);
        rating.setText(movie.getDisplayableRating());
        RatingUtils.loadRatingDrawableIntoView(movie.getRating(), rating);

        ((TextView) findViewById(R.id.overview)).setText(movie.getOverview());
        ((TextView) findViewById(R.id.release_date)).setText("Release Date: " + movie.getFormattedReleaseDate());

        addVideosFragment();
        addCreditsFragment();
        addRecommendedMoviesFragment();
    }

    private void subscribeToService() {
        subscription = MovieServiceProvider.getService().getMovieDetails(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::gotMovieWithDetails, this::gotError);
    }

    private void gotError(Throwable e) {
        e.printStackTrace();
        genresLayout.setMessage("Couldn't load genres\nTap to retry");
    }

    private void gotMovieWithDetails(Movie movie) {
        if (this.movie == null) {
            this.movie = movie;
            setBasicUI(movie);
        } else {
            this.movie.populateFrom(movie);
        }

        addGenres();
    }

    private void addGenres() {
        if (movie.getGenres().isEmpty()) {
            genresLayout.setMessage("No Genres Available");
        } else {
            genresLayout.setGenres(movie.getGenres());
        }
    }

    private void addVideosFragment() {
        MovieVideosFragment videosFragment = MovieVideosFragment.createInstance(movie);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.videos_layout, videosFragment)
                .commit();
    }

    private void addCreditsFragment() {
        Fragment fragment = MovieCreditsFragment.newInstance(movie);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.cast_layout, fragment)
                .commit();
    }

    private void addRecommendedMoviesFragment() {
        Fragment fragment = RecommendedMoviesFragment.newInstance(movie);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_recommended_movies, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DataBag.removeMovieFromPocket(movieId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null)
            subscription.unsubscribe();
    }
}
