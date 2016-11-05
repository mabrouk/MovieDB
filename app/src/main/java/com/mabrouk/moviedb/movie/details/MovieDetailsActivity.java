package com.mabrouk.moviedb.movie.details;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.common.DataBag;
import com.mabrouk.moviedb.common.ExternalUrlUtil;
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
    private int movieId;
    private Movie movie;

    Subscription subscription;

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
        setTitle(movie.getTitle());

        ImageView backdropImageView = (ImageView) findViewById(R.id.backdrop);
        Picasso.with(this).load(movie.getBackdropUrl()).into(backdropImageView);

        TextView rating = (TextView) findViewById(R.id.rating);
        rating.setText(movie.getDisplayableRating());

        GradientDrawable bgShape = (GradientDrawable)rating.getBackground();
        bgShape.setColor(Color.YELLOW);

        ((TextView) findViewById(R.id.overview)).setText(movie.getOverview());
        ((TextView) findViewById(R.id.release_date)).setText("Release Date: " + movie.getReleaseDate());

        addVideosFragment();
        addCreditsFragment();

        subscription = ServiceProvider.getService().getMovieDetails(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::gotMovieWithDetails, this::gotError);
    }

    private void gotError(Throwable e) {
        e.printStackTrace();
    }

    private void gotMovieWithDetails(Movie movie) {
        this.movie.populateFrom(movie);

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
        findViewById(R.id.genres_layout).findViewById(R.id.progressBar).setVisibility(View.GONE);

        //I really should throw this library and do it myself
        FlowLayout layout = (FlowLayout) findViewById(R.id.flow_layout);

        for (Genre genre : movie.getGenres()) {
            View buttonLayout = getLayoutInflater().inflate(R.layout.button_gener, null);
            Button button = (Button) buttonLayout.findViewById(R.id.button);
            button.setText(genre.getName());
            layout.addView(buttonLayout);
        }
    }

    private void addVideosFragment() {
        MovieVideosFragment videosFragment = new MovieVideosFragment();
        videosFragment.setMovie(movie);

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
