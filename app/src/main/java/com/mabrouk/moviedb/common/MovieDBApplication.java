package com.mabrouk.moviedb.common;

import android.app.Application;

/**
 * Created by VPN on 11/13/2016.
 */

public class MovieDBApplication extends Application {
    private static MovieDBApplication instance;

    public MovieDBApplication getInstance() {
        if (instance == null)
            throw new IllegalStateException("Application object isn't initialized properly");
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
