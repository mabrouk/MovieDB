package com.mabrouk.moviedb.common;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.movie.Movie;

/**
 * Created by VPN on 11/6/2016.
 */

public class RatingUtils {
    public static void loadRatingDrawableIntoView(Movie movie, View view) {
        double rating = movie.getRating();
        int drawableRes;
        if(rating <= 5)
            drawableRes = R.drawable.shape_red_circle;
        else if(rating < 7.5)
            drawableRes = R.drawable.shape_orange_circle;
        else
            drawableRes = R.drawable.shape_yellow_circle;

        Drawable ratingDrawable = view.getContext().getResources().getDrawable(drawableRes);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            view.setBackground(ratingDrawable);
        else
            view.setBackgroundDrawable(ratingDrawable);
    }
}
