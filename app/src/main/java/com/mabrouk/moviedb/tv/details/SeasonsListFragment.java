package com.mabrouk.moviedb.tv.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.tv.season.Season;

import java.util.List;

/**
 * Created by VPN on 11/17/2016.
 */

public class SeasonsListFragment extends Fragment {
    List<Season> seasons;
    public static SeasonsListFragment createInstance(List<Season> seasons) {
        SeasonsListFragment fragment = new SeasonsListFragment();
        fragment.seasons = seasons;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movie_details_horizontal_list, null);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        root.findViewById(R.id.progressBar).setVisibility(View.GONE);
        recyclerView.setAdapter(new SeasonsListAdapter(seasons));
        return root;
    }
}
