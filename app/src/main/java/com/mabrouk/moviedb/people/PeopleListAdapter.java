package com.mabrouk.moviedb.people;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.common.PagingAdapter;
import com.mabrouk.moviedb.movie.Movie;
import com.mabrouk.moviedb.network.MediaUrlBuilder;
import com.mabrouk.moviedb.people.details.PersonDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by VPN on 11/3/2016.
 */

public class PeopleListAdapter extends PagingAdapter<Person, PeopleListAdapter.PersonViewHolder> {
    int profileDimen;
    public PeopleListAdapter(Context context, Resources resources) {
        super(context);
        profileDimen = (int) resources.getDimension(R.dimen.person_thumb_dimen);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.row_person, parent, false);
        itemView.setOnClickListener(view -> {
            Person p = (Person) view.getTag();
            PersonDetailsActivity.startInstance(view.getContext(), p);
        });
        return new PersonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, Person data) {
        holder.itemView.setTag(data);
        holder.name.setText(data.name);

        StringBuilder builder = new StringBuilder(16 * 6);
        List<Movie> knownFor = data.getKnownFor();
        int size = knownFor.size();
        int min = size > 6 ? 6 : size;
        for(int i = 0; i < min && i < 6; i++) {
            builder.append(knownFor.get(i).getTitle());
            if(i < min - 1)
                builder.append(", ");
        }
        holder.knownFor.setText(builder.toString());
        String profileImageUrl = new MediaUrlBuilder(data.getProfilePath())
                .addType(MediaUrlBuilder.TYPE_PROFILE)
                .addSize(profileDimen, profileDimen)
                .build();
        Picasso.with(applicationContext).load(profileImageUrl).into(holder.thumbnail);
    }

    static class PersonViewHolder extends RecyclerView.ViewHolder{
        ImageView thumbnail;
        TextView name;
        TextView knownFor;
        public PersonViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            name = (TextView) itemView.findViewById(R.id.name);
            knownFor= (TextView) itemView.findViewById(R.id.known_for_text);
        }
    }
}
