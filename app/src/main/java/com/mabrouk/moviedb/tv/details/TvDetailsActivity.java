package com.mabrouk.moviedb.tv.details;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.common.DataBag;
import com.mabrouk.moviedb.genres.view.GenresLayout;
import com.mabrouk.moviedb.common.Utils.PaletteCreationUtils;
import com.mabrouk.moviedb.common.Utils.RatingUtils;
import com.mabrouk.moviedb.movie.details.MovieCreditsAdapter;
import com.mabrouk.moviedb.network.MediaUrlBuilder;
import com.mabrouk.moviedb.people.Person;
import com.mabrouk.moviedb.tv.Tv;
import com.mabrouk.moviedb.tv.api.TvServiceProvider;
import com.mabrouk.moviedb.tv.season.Season;
import com.mabrouk.moviedb.tv.season.SeasonActivity;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TvDetailsActivity extends AppCompatActivity {
    private static final String EXTRA_TV_ID = "tv_id";
    private static final String EXTRA_TITLE = "title";

    public static void startInstance(Tv tv, Activity activity) {
        DataBag.addTvToPocket(tv);
        startInstance(tv.getId(), tv.getName(), activity);
    }

    public static void startInstance(int tvId, String title, Activity activity) {
        Intent intent = new Intent(activity, TvDetailsActivity.class);
        intent.putExtra(EXTRA_TV_ID, tvId);
        intent.putExtra(EXTRA_TITLE, title);
        activity.startActivity(intent);
    }

    TextView overview;
    TextView firstAirDate;
    TextView rating;
    TextView createdBy;
    ImageView backdrop;
    GenresLayout genresLayout;
    Toolbar toolbar;

    int showId;
    Tv show;
    Subscription subscription;

    int primaryColor;
    int darkPrimaryColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tv_details);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getIntent().getStringExtra(EXTRA_TITLE));

        showId = getIntent().getIntExtra(EXTRA_TV_ID, 0);

        show = DataBag.getTvFromPocket(showId);
        subscription = TvServiceProvider.getService().getTvDetails(showId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::gotTvDetails, this::gotError);
        overview = (TextView) findViewById(R.id.overview);
        firstAirDate = (TextView) findViewById(R.id.first_air_date);
        rating = (TextView) findViewById(R.id.rating);
        createdBy = (TextView) findViewById(R.id.created_by);
        backdrop = (ImageView) findViewById(R.id.backdrop);
        genresLayout = (GenresLayout) findViewById(R.id.genres_layout);

        primaryColor = getResources().getColor(R.color.colorPrimary);
        darkPrimaryColor = getResources().getColor(R.color.colorPrimaryDark);

        if(show != null)
            setupBasicUI();
    }

    private void setupBasicUI() {
        overview.setText(show.getOverview());
        firstAirDate.setText("First aired: " + show.getFirstAirDateFormatted());
        rating.setText(show.getDisplayableRating());
        RatingUtils.loadRatingDrawableIntoView(show.getRating(), rating);
        addRecommendedShowsFragment();
        addVideosFragment();
        loadBackdrop();
    }

    private void loadBackdrop() {
        String backdropUrl = new MediaUrlBuilder(show.getBackdropPath())
                .addType(MediaUrlBuilder.TYPE_BACKDROP)
                .build();

        PaletteCreationUtils.loadBackdrop(backdropUrl, backdrop, palette -> {
            darkPrimaryColor = palette.getDarkMutedColor(darkPrimaryColor);
            primaryColor = palette.getMutedColor(primaryColor);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                getWindow().setStatusBarColor(darkPrimaryColor);
            CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
            toolbarLayout.setContentScrimColor(primaryColor);
        });
    }

    public void onSeasonClicked(Season season) {
        SeasonActivity.startInstance(this, season.getSeasonNumber(), show, primaryColor, darkPrimaryColor);
    }

    private void addRecommendedShowsFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_recommended_shows, RecommendedShowsFragment.createInstance(show))
                .commit();
    }

    private void gotTvDetails(Tv tv) {
        if(show == null) {
            show = tv;
            setupBasicUI();
        }else{
            show.populateFrom(tv);
        }

        StringBuilder builder = new StringBuilder(show.getCreatedBy().size() * 16); //16 is a rough estimation for a name char count
        for(Person creator : show.getCreatedBy()) {
            builder.append(creator.getName());
            builder.append(",");
        }

        builder.deleteCharAt(builder.lastIndexOf(","));
        createdBy.setText("Created by: " + builder.toString());

        displayGenres();
        displayCast();
        displaySeasons();
    }

    private void gotError(Throwable e) {
        e.printStackTrace();
    }

    private void displayGenres() {
        if(show.getGenres().isEmpty()) {
            genresLayout.setMessage("No Genres Available");
        } else {
            genresLayout.setGenres(show.getGenres());
            genresLayout.setOnGenreClickedListener(genre -> Toast.makeText(this,
                    "Tv shows filter by genre isn't supported currently by the MovieDB service", Toast.LENGTH_SHORT).show());
        }
    }

    private void displayCast() {
        View castLayout = findViewById(R.id.cast_layout);
        RecyclerView recyclerView = (RecyclerView) castLayout.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(new MovieCreditsAdapter(show.getCredits(), getResources()));
        castLayout.findViewById(R.id.progressBar).setVisibility(View.GONE);
    }

    private void displaySeasons() {
        SeasonsListFragment fragment = SeasonsListFragment.createInstance(show.getSeasons());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.seasons_layout, fragment)
                .commit();
    }

    private void addVideosFragment() {
        TvVideosFragment fragment = TvVideosFragment.createInstance(show);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.videos_layout, fragment)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DataBag.removeTvFromPocket(showId);
    }
}
