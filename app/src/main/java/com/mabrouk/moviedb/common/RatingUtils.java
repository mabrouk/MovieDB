package com.mabrouk.moviedb.common;

import android.view.View;

import com.mabrouk.moviedb.R;

/**
 * Created by VPN on 11/6/2016.
 */

public class RatingUtils {
    public static void loadRatingDrawableIntoView(double rating, View view) {
        int drawableRes;
        if(rating <= 5)
            drawableRes = R.drawable.shape_red_circle;
        else if(rating < 7.5)
            drawableRes = R.drawable.shape_orange_circle;
        else
            drawableRes = R.drawable.shape_yellow_circle;

            view.setBackgroundResource(drawableRes);
    }
}
