package com.mabrouk.moviedb.tv.details;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.tv.Tv;
import com.mabrouk.moviedb.tv.season.Season;
import com.mabrouk.moviedb.tv.season.SeasonActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by VPN on 11/17/2016.
 */

public class SeasonsListAdapter extends RecyclerView.Adapter<SeasonsListAdapter.SeasonsListViewHolder>{
    List<Season> seasons;
    public SeasonsListAdapter(List<Season> seasons) {
        this.seasons = seasons;
    }

    @Override
    public SeasonsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_season, parent, false);
        return new SeasonsListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SeasonsListViewHolder holder, int position) {
        Season season = seasons.get(position);
        holder.seasonInfo.setText(String.format("Season %d (%d episodes)", season.getSeasonNumber(), season.getEpisodeCount()));
        Picasso.with(holder.itemView.getContext()).load(season.getPosterThum()).into(holder.posterThum);
        holder.itemView.setOnClickListener( view ->
                ((TvDetailsActivity) view.getContext()).onSeasonClicked(season)
        );
    }

    @Override
    public int getItemCount() {
        return seasons.size();
    }

    static class SeasonsListViewHolder extends RecyclerView.ViewHolder {
        ImageView posterThum;
        TextView seasonInfo;
        public SeasonsListViewHolder(View itemView) {
            super(itemView);
            posterThum = (ImageView) itemView.findViewById(R.id.poster);
            seasonInfo = (TextView) itemView.findViewById(R.id.season_info);
        }
    }
}
