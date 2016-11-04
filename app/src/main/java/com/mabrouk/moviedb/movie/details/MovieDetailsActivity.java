package com.mabrouk.moviedb.movie.details;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.common.DataBag;
import com.mabrouk.moviedb.movie.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE_ID = "movie_id";
    private int movieId;
    private Movie movie;

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DataBag.removeMovieFromPocket(movieId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
