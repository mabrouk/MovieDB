package com.mabrouk.moviedb.movie;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.mabrouk.moviedb.R;

import java.util.List;

import rx.Observable;


public class MovieListFragment extends Fragment implements MoviePagesLoader.PageLoadedListener{
    private RecyclerView moviesList;
    private MovieListAdapter adapter;
    private MoviePagesLoader pagesLoader;
    private ProgressBar progressBar;

    public void setPagesLoader(MoviePagesLoader pagesLoader) {
        this.pagesLoader = pagesLoader;
        pagesLoader.listenForPageLoaded(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movie_list, container, false);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        moviesList = (RecyclerView) root.findViewById(R.id.movies_list);

        if(adapter == null)
            adapter = new MovieListAdapter(root.getContext());
        else
            progressBar.setVisibility(View.INVISIBLE);

        moviesList.setAdapter(adapter);

        moviesList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) moviesList.getLayoutManager();
                int position = layoutManager.findLastVisibleItemPosition();

                if (position == layoutManager.getItemCount() -1)
                    needsNewPage();
            }
        });

        return root;
    }

    private void needsNewPage() {
        pagesLoader.loadNextPage();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pagesLoader.stopListeningForPageLoaded(this);
    }

    @Override
    public void pageLoaded(List<Movie> page) {
        if(page.size() > 0)
            adapter.addMovies(page);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void pageLoadFailed() {
        //TODO show a snackbar
        progressBar.setVisibility(View.INVISIBLE);
    }
}
