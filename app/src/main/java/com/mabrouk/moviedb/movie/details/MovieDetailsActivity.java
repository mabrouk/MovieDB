package com.mabrouk.moviedb.movie.details;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.common.DataBag;
import com.mabrouk.moviedb.genres.MovieByGenreActivity;
import com.mabrouk.moviedb.genres.view.GenresLayout;
import com.mabrouk.moviedb.common.Utils.PaletteCreationUtils;
import com.mabrouk.moviedb.common.Utils.RatingUtils;
import com.mabrouk.moviedb.movie.Movie;
import com.mabrouk.moviedb.movie.api.MovieServiceProvider;
import com.mabrouk.moviedb.network.MediaUrlBuilder;

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
        TextView rating = (TextView) findViewById(R.id.rating);

        rating.setText(movie.getDisplayableRating());
        RatingUtils.loadRatingDrawableIntoView(movie.getRating(), rating);

        ((TextView) findViewById(R.id.overview)).setText(movie.getOverview());
        ((TextView) findViewById(R.id.release_date)).setText("Release Date: " + movie.getFormattedReleaseDate());

        loadBackdropPalette();

        addVideosFragment();
        addCreditsFragment();
        addRecommendedMoviesFragment();
    }

    void loadBackdropPalette() {
        ImageView backdropImageView = (ImageView) findViewById(R.id.backdrop);
        String backdropUrl = new MediaUrlBuilder(movie.getBackdropPath())
                .addType(MediaUrlBuilder.TYPE_POSTER)
                .build();

        PaletteCreationUtils.loadBackdrop(backdropUrl, backdropImageView, palette -> {
            int colorPrimary = palette.getMutedColor(getResources().getColor(R.color.colorPrimary));
            int colorPrimaryDark = palette.getDarkMutedColor(getResources().getColor(R.color.colorPrimaryDark));
            ((CollapsingToolbarLayout) findViewById(R.id.toolbar_layout)).setContentScrimColor(colorPrimary);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                getWindow().setStatusBarColor(colorPrimaryDark);
        });
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
            genresLayout.setOnGenreClickedListener(genre -> MovieByGenreActivity.startInstance(this, genre));
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
