package com.mabrouk.moviedb.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VPN on 11/3/2016.
 */

public abstract class PagingAdapter<T extends BaseModel,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>{
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
    public void onBindViewHolder(VH holder, int position) {
        onBindViewHolder(holder, list.get(position));
    }

    public abstract void onBindViewHolder(VH holder, T data);

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateViewHolder(LayoutInflater.from(parent.getContext()), parent, viewType);
    }

    public abstract VH onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType);

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }
}
