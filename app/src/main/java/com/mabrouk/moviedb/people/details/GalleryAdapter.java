package com.mabrouk.moviedb.people.details;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.common.ImageViewerActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by User on 11/8/2016.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>{
    List<ProfileImage> images;

    public GalleryAdapter(List<ProfileImage> images) {
        this.images = images;
    }

    @Override
    public GalleryAdapter.GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        int height = (int) parent.getContext().getResources().getDimension(R.dimen.gallery_image_height);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height));
        return new GalleryViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(GalleryAdapter.GalleryViewHolder holder, int position) {
        ImageView imageView = (ImageView) holder.itemView;
        ProfileImage image = images.get(position);
        imageView.setOnClickListener(view ->
                ImageViewerActivity.startInstance(imageView.getContext(), image.getOriginalUrl(), image.getThumbnailUrl()));
        Picasso.with(imageView.getContext()).load(images.get(position).getThumbnailUrl()).into(imageView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    static class GalleryViewHolder extends RecyclerView.ViewHolder {

        public GalleryViewHolder(View itemView) {
            super(itemView);
        }
    }
}
