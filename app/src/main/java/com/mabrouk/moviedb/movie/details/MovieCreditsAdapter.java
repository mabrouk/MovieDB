package com.mabrouk.moviedb.movie.details;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.people.Cast;
import com.mabrouk.moviedb.people.Crew;
import com.squareup.picasso.Picasso;

/**
 * Created by VPN on 11/4/2016.
 */

public class MovieCreditsAdapter extends RecyclerView.Adapter<MovieCreditsAdapter.CreditsViewHolder> {
    MovieCredits credits;

    public MovieCreditsAdapter(MovieCredits credits) {
        this.credits = credits;
    }

    @Override
    public CreditsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_credits, parent, false);
        return new CreditsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CreditsViewHolder holder, int position) {
        String profileUrl;
//        if(position < credits.crew.size()) {
//            Crew crew = credits.crew.get(position);
//            holder.name.setText(crew.getName());
//            holder.role.setText(crew.getJob());
//            profileUrl = crew.getProfileUrl();
//        }else{
            Cast cast = credits.cast.get(position - credits.crew.size());
            holder.name.setText(cast.getName());
            holder.role.setText(cast.getCharacter());
            profileUrl = cast.getProfileUrl();
//        }
        Picasso.with(holder.itemView.getContext()).load(profileUrl).into(holder.profilePic);
    }

    @Override
    public int getItemCount() {
        return credits.cast.size();
    }

    static class CreditsViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePic;
        TextView name;
        TextView role;

        public CreditsViewHolder(View itemView) {
            super(itemView);
            profilePic = (ImageView) itemView.findViewById(R.id.profile_pic);
            name = (TextView) itemView.findViewById(R.id.name);
            role = (TextView) itemView.findViewById(R.id.role);
        }
    }
}
