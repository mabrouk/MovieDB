package com.mabrouk.moviedb.common.Utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.graphics.Palette;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by VPN on 11/17/2016.
 */

public class PaletteCreationUtils {
    public static void loadBackdrop(String url, ImageView imageView, Palette.PaletteAsyncListener listener) {
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                imageView.setImageBitmap(bitmap);
                Palette.from(bitmap).generate(listener);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {}

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}
        };
        imageView.setTag(target);
        Picasso.with(imageView.getContext()).load(url).into(target);
    }
}
