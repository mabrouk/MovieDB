package com.mabrouk.moviedb.movie.details;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.common.DataBag;
import com.mabrouk.moviedb.common.ExternalUrlUtil;
import com.mabrouk.moviedb.common.RatingUtils;
import com.mabrouk.moviedb.common.WebviewActivity;
import com.mabrouk.moviedb.genres.Genre;
import com.mabrouk.moviedb.movie.Movie;
import com.mabrouk.moviedb.movie.api.ServiceProvider;
import com.squareup.picasso.Picasso;

import org.apmem.tools.layouts.FlowLayout;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MovieDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE_ID = "movie_id";

    public static void startMovieDetailsActivity(Context context, Movie movie) {
        DataBag.addMovieToPocket(movie);
        startMovieDetailsActivity(context, movie.getId());
    }

    public static void startMovieDetailsActivity(Context context, int movieId) {
        Intent intent = new Intent(context, MovieDetailsActivity.class);
        intent.putExtra(MovieDetailsActivity.EXTRA_MOVIE_ID, movieId);
        context.startActivity(intent);
    }

    private int movieId;
    private Movie movie;

    Subscription subscription;
    TextView genresTextView;
    ProgressBar genresProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        movieId = getIntent().getIntExtra(EXTRA_MOVIE_ID, 0);
        if (movieId == 0)
            throw new IllegalStateException("MovieDetailsActivity intent must contain movie id");
        movie = DataBag.getMovieFromPocket(movieId);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(movie != null) {
            setBasicUI(movie);
        }

        genresTextView = (TextView) findViewById(R.id.genres_textview);
        genresProgress = (ProgressBar) findViewById(R.id.progressBar);

       subscribeToService();
    }

    private void setBasicUI(Movie movie) {
        ImageView backdropImageView = (ImageView) findViewById(R.id.backdrop);
        TextView rating = (TextView) findViewById(R.id.rating);

        setTitle(movie.getTitle());
        Picasso.with(this).load(movie.getBackdropUrl()).into(backdropImageView);
        rating.setText(movie.getDisplayableRating());
        RatingUtils.loadRatingDrawableIntoView(movie, rating);

        ((TextView) findViewById(R.id.overview)).setText(movie.getOverview());
        ((TextView) findViewById(R.id.release_date)).setText("Release Date: " + movie.getFormattedReleaseDate());

        addVideosFragment();
        addCreditsFragment();
        addRecommendedMoviesFragment();
    }

    private void subscribeToService() {
        subscription = ServiceProvider.getService().getMovieDetails(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::gotMovieWithDetails, this::gotError);
    }

    private void gotError(Throwable e) {
        e.printStackTrace();
        genresTextView.setVisibility(View.VISIBLE);
        genresTextView.setText("Couldn't load genres\nTap to retry");
        genresTextView.setOnClickListener(view -> {
            genresTextView.setVisibility(View.GONE);
            genresProgress.setVisibility(View.VISIBLE);
            subscribeToService();
        });
        genresProgress.setVisibility(View.INVISIBLE);
    }

    private void gotMovieWithDetails(Movie movie) {
        if(this.movie == null) {
            this.movie = movie;
            setBasicUI(movie);
        } else {
            this.movie.populateFrom(movie);
        }

        if(!movie.getImdb().isEmpty()) {
            View imdbButton = findViewById(R.id.imdb_btn);
            imdbButton.setVisibility(View.VISIBLE);
            imdbButton.setOnClickListener(this::openUrl);
        }

        if(!movie.getWebsite().isEmpty()) {
            View websiteButton = findViewById(R.id.homepage_btn);
            websiteButton.setVisibility(View.VISIBLE);
            websiteButton.setOnClickListener(this::openUrl);
        }

        addGenres();
    }

    private void addGenres() {
        genresProgress.setVisibility(View.GONE);

        if(movie.getGenres().isEmpty()) {
            genresTextView.setText("No genres available");
            genresTextView.setVisibility(View.VISIBLE);
        }else{
            genresTextView.setVisibility(View.GONE);
            FlowLayout layout = (FlowLayout) findViewById(R.id.flow_layout);
            int backgroundResource = Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP ? R.drawable.shape_rounded_rect
                    : R.drawable.genre_ripple_background;

            for (Genre genre : movie.getGenres()) {
                View buttonLayout = getLayoutInflater().inflate(R.layout.button_gener, null);
                Button button = (Button) buttonLayout.findViewById(R.id.button);
                button.setBackgroundResource(backgroundResource);
                button.setText(genre.getName());
                layout.addView(buttonLayout);
            }
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

    private void openUrl(View v) {
        String url = v.getId() == R.id.homepage_btn ? movie.getWebsite()
                : ExternalUrlUtil.IMDBUrlForTitle(movie.getImdb());
        Intent intent = new Intent(this, WebviewActivity.class);
        intent.putExtra(WebviewActivity.EXTRA_TITLE, movie.getTitle());
        intent.putExtra(WebviewActivity.EXTRA_URL, url);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DataBag.removeMovieFromPocket(movieId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(subscription != null)
            subscription.unsubscribe();
    }
}
