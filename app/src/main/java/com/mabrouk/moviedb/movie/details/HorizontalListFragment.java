package com.mabrouk.moviedb.movie.details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.common.ResultList;
import com.mabrouk.moviedb.movie.Movie;
import com.mabrouk.moviedb.movie.api.ServiceProvider;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by VPN on 11/5/2016.
 */

public abstract class HorizontalListFragment extends Fragment{
    protected RecyclerView recyclerView;
    Subscription subscription;
    protected Movie movie;
    protected ProgressBar progressBar;
    TextView errorText;
    protected TextView emptyText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movie_details_horizontal_list, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        emptyText = (TextView) root.findViewById(R.id.empty_textview);
        errorText = (TextView) root.findViewById(R.id.error_textview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        downloadData();
        return root;
    }

    private void downloadData() {
        progressBar.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.GONE);
        subscribeToService();
    }

    abstract protected void subscribeToService();

    protected void gotError(Throwable e) {
        errorText.setVisibility(View.VISIBLE);
        errorText.setOnClickListener(view -> downloadData());
        errorText.setText(getErrorMessage());
        progressBar.setVisibility(View.INVISIBLE);
    }

    abstract protected String getErrorMessage();

    abstract protected String getEmptyMessage();

    protected void showEmptyMessage() {
        emptyText.setVisibility(View.VISIBLE);
        emptyText.setText(getEmptyMessage());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null)
            subscription.unsubscribe();
    }
}
