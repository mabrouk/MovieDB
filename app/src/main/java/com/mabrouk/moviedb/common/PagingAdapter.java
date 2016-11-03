package com.mabrouk.moviedb.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.mabrouk.moviedb.movie.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VPN on 11/3/2016.
 */

public abstract class PagingAdapter<T,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>{
    protected List<T> list = new ArrayList<>();
    protected Context applicationContext;

    public PagingAdapter(Context context) {
        setHasStableIds(true);
        applicationContext = context.getApplicationContext();
    }

    public void addPage(List<T> page) {
        int positionStart = list.size();
        list.addAll(page);
        notifyItemRangeInserted(positionStart, list.size());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
