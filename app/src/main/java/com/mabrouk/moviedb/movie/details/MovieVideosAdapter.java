package com.mabrouk.moviedb.movie.details;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.common.Utils.ExternalUrlUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VPN on 11/4/2016.
 */

public class MovieVideosAdapter extends RecyclerView.Adapter<MovieVideosAdapter.VideoViewHolder> {
    List<MovieVideo> videos = new ArrayList<>();

    public MovieVideosAdapter(List<MovieVideo> videos) {
        this.videos = videos;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_movie_video, parent, false);
        itemView.setOnClickListener(view -> {
            MovieVideo video = (MovieVideo) view.getTag();
            String youtubeUrl = ExternalUrlUtil.youtubeUrlForKey(video.key);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl));
            itemView.getContext().startActivity(intent);
        });
        return new VideoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        MovieVideo video = videos.get(position);
        holder.itemView.setTag(video);
        holder.type.setText(video.getType());
        holder.name.setText(video.getName());
        Picasso.with(holder.itemView.getContext()).load(video.getThumbnailUrl()).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView name;
        TextView type;

        public VideoViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            name = (TextView) itemView.findViewById(R.id.video_name);
            type = (TextView) itemView.findViewById(R.id.video_type);
        }
    }
}
