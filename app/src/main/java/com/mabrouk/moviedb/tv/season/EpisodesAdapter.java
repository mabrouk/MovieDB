package com.mabrouk.moviedb.tv.season;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mabrouk.moviedb.R;

import java.util.List;

/**
 * Created by VPN on 11/21/2016.
 */

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.EpisodeViewHolder> {
    List<Episode> episodes;
    public EpisodesAdapter(List<Episode> episodes) {
        this.episodes = episodes;
    }

    @Override
    public EpisodeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_episode, parent, false);
        return new EpisodeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EpisodeViewHolder holder, int position) {
        Episode episode = episodes.get(position);
        holder.overview.setText(episode.overview);
        holder.airDate.setText("Air date: " + episode.getAirDateFormatted());
        holder.episodeNumber.setText("#" + episode.getNumber());
        holder.episodeName.setText(episode.getName());
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    static class EpisodeViewHolder extends RecyclerView.ViewHolder {
        TextView episodeNumber;
        TextView episodeName;
        TextView airDate;
        TextView overview;

        public EpisodeViewHolder(View itemView) {
            super(itemView);
            episodeName = (TextView) itemView.findViewById(R.id.episode_name);
            episodeNumber = (TextView) itemView.findViewById(R.id.episode_number);
            airDate = (TextView) itemView.findViewById(R.id.air_date);
            overview = (TextView) itemView.findViewById(R.id.overview);
        }
    }
}
