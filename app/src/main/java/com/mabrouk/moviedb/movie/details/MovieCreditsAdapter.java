package com.mabrouk.moviedb.movie.details;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.network.MediaUrlBuilder;
import com.mabrouk.moviedb.people.details.PersonDetailsActivity;
import com.squareup.picasso.Picasso;

/**
 * Created by VPN on 11/4/2016.
 */

public class MovieCreditsAdapter extends RecyclerView.Adapter<MovieCreditsAdapter.CreditsViewHolder> {
    MovieCredits credits;
    int profileDimen;

    public MovieCreditsAdapter(MovieCredits credits, Resources resources) {
        this.credits = credits;
        profileDimen = (int) resources.getDimension(R.dimen.person_large_thumb_dimen);
    }

    @Override
    public CreditsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_credits, parent, false);
        return new CreditsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CreditsViewHolder holder, int position) {
        Cast cast = credits.cast.get(position);
        String profileUrl = new MediaUrlBuilder(cast.getProfilePath())
                .addSize(profileDimen, profileDimen)
                .addType(MediaUrlBuilder.TYPE_PROFILE)
                .build();
        holder.name.setText(cast.getName());
        holder.role.setText(cast.getCharacter());
        holder.itemView.setOnClickListener(view -> PersonDetailsActivity.startInstance(view.getContext(), cast));
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
