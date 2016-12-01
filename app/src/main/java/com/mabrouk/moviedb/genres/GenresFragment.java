package com.mabrouk.moviedb.genres;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.mabrouk.moviedb.R;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by VPN on 12/1/2016.
 */

public class GenresFragment extends Fragment {
    List<Genre> movieGenres;
    ProgressBar progressBar;
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_genres, container, false);
        listView = (ListView) rootView.findViewById(R.id.list_view);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        subscribeToService();
        return rootView;
    }

    void subscribeToService() {
        progressBar.setVisibility(View.VISIBLE);
        GenresServiceProvider.getMovieGenres()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::gotGenresList, this::gotError);
    }

    void gotGenresList(List<Genre> genres) {
        progressBar.setVisibility(View.GONE);
        this.movieGenres = genres;
        listView.setAdapter(new GenresAdapter(genres));
    }

    void gotError(Throwable throwable) {
        progressBar.setVisibility(View.INVISIBLE);
    }
}
