package com.mabrouk.moviedb.tv.season;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.common.DataBag;
import com.mabrouk.moviedb.common.PaletteCreationUtils;
import com.mabrouk.moviedb.tv.Tv;
import com.mabrouk.moviedb.tv.TvServiceProvider;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SeasonActivity extends AppCompatActivity {
    private static final String EXTRA_SEASON_NUMBER = "season_number";
    private static final String EXTRA_TV_ID = "tv_id";
    private static final String EXTRA_PRIMARY_COLOR = "primary_color";
    private static final String EXTRA_DARK_COLOR = "dark_color";

    public static void startInstance(Activity activity, int seasonNumber, Tv tv, int primaryColor, int darkColor) {
        Intent intent = new Intent(activity, SeasonActivity.class);
        intent.putExtra(EXTRA_SEASON_NUMBER, seasonNumber);
        intent.putExtra(EXTRA_TV_ID, tv.getId());
        intent.putExtra(EXTRA_DARK_COLOR, darkColor);
        intent.putExtra(EXTRA_PRIMARY_COLOR, primaryColor);
        DataBag.addTvToPocket(tv);
        activity.startActivity(intent);
    }
    int seasonNumber;
    int tvId;
    Season season;

    ProgressBar progressBar;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        tvId = getIntent().getIntExtra(EXTRA_TV_ID, 0);
        seasonNumber = getIntent().getIntExtra(EXTRA_SEASON_NUMBER, 0);
        Tv tv = DataBag.getTvFromPocket(tvId);
        List<Season> seasons = tv.getSeasons();
        for(Season season : seasons) {
            if(season.getSeasonNumber() == seasonNumber) {
                this.season = season;
                break;
            }
        }
        toolbar.setSubtitle(tv.getName());
        toolbar.setTitle("Season " + season.getSeasonNumber());
        toolbar.setBackgroundColor(getIntent().getIntExtra(EXTRA_PRIMARY_COLOR, 0));
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(getIntent().getIntExtra(EXTRA_DARK_COLOR, 0));

        TvServiceProvider.getService().getSeason(tvId, seasonNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::gotSeason);

        toolbar.setTitleTextColor(0xffffffff);
        toolbar.setSubtitleTextColor(0xffeeeeee);
    }

    private void gotSeason(Season season) {
        this.season.populateFrom(season);
        progressBar.setVisibility(View.GONE);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setAdapter(new EpisodesAdapter(season.getEpisodes()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataBag.removeTvFromPocket(tvId);
    }
}
