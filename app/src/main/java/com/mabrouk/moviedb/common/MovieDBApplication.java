package com.mabrouk.moviedb.common;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.mabrouk.moviedb.configurations.ConfigurationServiceProvider;
import com.mabrouk.moviedb.configurations.ConfigurationsStore;

/**
 * Created by VPN on 11/13/2016.
 */

public class MovieDBApplication extends Application {
    private static MovieDBApplication instance;

    public static void initConstants() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager manager = (WindowManager) instance.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(displayMetrics);

        ScreenConstants.init(displayMetrics.widthPixels, displayMetrics.heightPixels,
                displayMetrics.densityDpi, displayMetrics.density);
    }


    public static MovieDBApplication getInstance() {
        if (instance == null)
            throw new IllegalStateException("Application object isn't initialized properly");
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        ConfigurationsStore.init();
        initConstants();

        ConfigurationServiceProvider.syncConfigurationsIfNeeded();
    }
}
