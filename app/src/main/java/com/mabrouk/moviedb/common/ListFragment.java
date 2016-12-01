package com.mabrouk.moviedb.common;

import android.content.Context;
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

/**
 * Created by VPN on 11/2/2016.
 */

public abstract class ListFragment<T extends BaseModel> extends Fragment implements PagesLoader.PageLoadedListener<T>{
    private RecyclerView recyclerView;
    protected PagingAdapter<T, ?> adapter;
    private PagesLoader pagesLoader;
    private ProgressBar progressBar;

    public void setPagesLoader(PagesLoader pagesLoader) {
        this.pagesLoader = pagesLoader;
        pagesLoader.listenForPageLoaded(this);
        if(adapter != null) {
            adapter.clear();
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) root.findViewById(R.id.movies_list);

        if(adapter == null)
            adapter = initAdapter(root.getContext());
        else
            progressBar.setVisibility(View.INVISIBLE);

        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) ListFragment.this.recyclerView.getLayoutManager();
                int position = layoutManager.findLastVisibleItemPosition();

                if (position == layoutManager.getItemCount() -1)
                    needsNewPage();
            }
        });

        return root;
    }

    protected abstract PagingAdapter<T, ?> initAdapter(Context context);

    private void needsNewPage() {
        if(pagesLoader.hasMorePages()) {
            pagesLoader.loadNextPage();
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pagesLoader.stopListeningForPageLoaded(this);
    }

    @Override
    public void pageLoaded(List<T> page) {
        if(page.size() > 0)
            adapter.addPage(page);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void pageLoadFailed() {
        //TODO show a snackbar
        progressBar.setVisibility(View.INVISIBLE);
    }
}
