package com.mabrouk.moviedb.network;

import android.support.annotation.IntDef;

import com.mabrouk.moviedb.common.ScreenConstants;
import com.mabrouk.moviedb.configurations.ConfigurationDefaults;
import com.mabrouk.moviedb.configurations.ConfigurationSelector;
import com.mabrouk.moviedb.configurations.ConfigurationsStore;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by VPN on 11/13/2016.
 */

public class MediaUrlBuilder {
    @Retention(RetentionPolicy.CLASS)
    @IntDef({TYPE_BACKDROP, TYPE_PROFILE, TYPE_POSTER})
    @interface MediaType {}
    public static final int TYPE_POSTER = 1;
    public static final int TYPE_BACKDROP = 2;
    public static final int TYPE_PROFILE = 3;

    String path;
    int width, height;
    int mediaType;
    ConfigurationSelector[] selectors;

    public MediaUrlBuilder(String path) {
        this.path = path;
        selectors = new ConfigurationSelector[] {
                new ConfigurationSelector(ConfigurationsStore.getPosterConfigurations()),
                new ConfigurationSelector(ConfigurationsStore.getBackdropConfigurations()),
                new ConfigurationSelector(ConfigurationsStore.getProfileConfigurations())
        };

        mediaType = 0;  //no media type

        width = ScreenConstants.getScreenWidth();
        height = ScreenConstants.getScreenHeight();
    }

    public MediaUrlBuilder addSize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public MediaUrlBuilder addType(@MediaType int type) {
        this.mediaType = type;
        return this;
    }

    /**
     * Builds the url for the specified resource based on the build parameters passed to the builder.
     * If no media type passed through {@link #addType}, the returned url will be the full size of the image
     * If no width and height passed through {@link #addSize(int, int)}, default width and height will be set
     * which will be the width and height of the device
     *
     * @return url for the specified resource
     */
    public String build() {
        String baseUrl = ConfigurationDefaults.IMAGES_DEFAULT_BASE_URL;
        String configPath;
        if(mediaType == 0) {
            configPath = "original";
        } else {
            ConfigurationSelector selector = selectors[mediaType - 1];
            configPath = selector.configPathForSize(width, height);
        }
        return baseUrl + configPath + path;
    }
}
