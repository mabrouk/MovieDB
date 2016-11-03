package com.mabrouk.moviedb.people;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.common.PagingAdapter;
import com.squareup.picasso.Picasso;

/**
 * Created by VPN on 11/3/2016.
 */

public class PeopleListAdapter extends PagingAdapter<Person, PeopleListAdapter.PersonViewHolder> {

    public PeopleListAdapter(Context context) {
        super(context);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.row_person, parent, false);

        return new PersonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, Person data) {
        holder.name.setText(data.name);
        Picasso.with(applicationContext).load(data.getThumbnail()).into(holder.thumbnail);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).id;
    }

    static class PersonViewHolder extends RecyclerView.ViewHolder{
        ImageView thumbnail;
        TextView name;
        public PersonViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            name = (TextView) itemView.findViewById(R.id.name);
        }
    }
}
