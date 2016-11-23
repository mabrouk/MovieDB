package com.mabrouk.moviedb.people.details;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.common.ScreenConstants;
import com.mabrouk.moviedb.network.MediaUrlBuilder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by User on 11/8/2016.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>{
    List<ProfileImage> images;
    int height;
    public GalleryAdapter(List<ProfileImage> images) {
        this.images = images;
    }

    @Override
    public GalleryAdapter.GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        height = (int) parent.getContext().getResources().getDimension(R.dimen.gallery_image_height);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height));
        return new GalleryViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(GalleryAdapter.GalleryViewHolder holder, int position) {
        ImageView imageView = (ImageView) holder.itemView;
        final ProfileImage image = images.get(position);
        final String profileImageUrl = new MediaUrlBuilder(image.filePath)
                .addType(MediaUrlBuilder.TYPE_PROFILE)
                .addSize(ScreenConstants.getScreenWidth() / 2, height)
                .build();
        Picasso.with(imageView.getContext()).load(profileImageUrl).into(imageView);
        imageView.setOnClickListener(view->
                GalleryActivity.startInstance((Activity) view.getContext(), image, profileImageUrl));
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
