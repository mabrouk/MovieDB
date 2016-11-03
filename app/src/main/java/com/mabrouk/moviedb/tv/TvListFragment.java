package com.mabrouk.moviedb.tv;

import android.content.Context;

import com.mabrouk.moviedb.common.ListFragment;
import com.mabrouk.moviedb.common.PagingAdapter;

/**
 * Created by VPN on 11/3/2016.
 */

public class TvListFragment extends ListFragment<Tv>{

    @Override
    protected PagingAdapter<Tv, ?> initAdapter(Context context) {
        return new TvListAdapter(context);
    }
}
