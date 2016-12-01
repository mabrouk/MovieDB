package com.mabrouk.moviedb.configurations;

import android.content.Context;
import android.content.SharedPreferences;

import com.mabrouk.moviedb.common.MovieDBApplication;
import com.mabrouk.moviedb.network.ApiInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by VPN on 11/13/2016.
 */

public final class ConfigurationsStore {
    private static final String PREF_BACKDROP_CONFIG = "backdrop_config";
    private static final String PREF_PROFILE_CONFIG = "profile_config";
    private static final String PREF_POSTER_CONFIG = "poster_config";
    private static final String PREF_SECURE_IMAGE_URL = "image_base_url";
    private static final String PREFERENCES_NAME = "movie_db_shared";
    private static final String PREF_CONFIG_UPDATED_AT = "conf_updated_at";

    private static List<String> posterConfigurations;
    private static List<String> profileConfigurations;
    private static List<String> backdropConfigurations;

    private static SharedPreferences sharedPreferences;

    private ConfigurationsStore() {
        throw new IllegalStateException("Cannot construct objects of this class");
    }

    public static void init() {
        if (profileConfigurations != null)
            throw new IllegalStateException("ConfigurationStore could only be initialized once");

        sharedPreferences = MovieDBApplication.getInstance()
                .getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        backdropConfigurations = loadBackdropConfigurations();
        posterConfigurations = loadPosterConfigurations();
        profileConfigurations = loadProfileConfigurations();

        ApiInfo.IMAGES_BASE_URL = sharedPreferences.getString(PREF_SECURE_IMAGE_URL, ConfigurationDefaults.IMAGES_DEFAULT_BASE_URL);
    }

    // most probably this will only used for testing
    public static void initWithDefaults() {
        if (profileConfigurations != null)
            throw new IllegalStateException("ConfigurationStore could only be initialized once");
        backdropConfigurations = Arrays.asList(ConfigurationDefaults.BACKDROP_DEFAULT_CONFIGURATIONS);
        profileConfigurations = Arrays.asList(ConfigurationDefaults.PROFILE_DEFAULT_CONFIGURATIONS);
        posterConfigurations = Arrays.asList(ConfigurationDefaults.POSTER_DEFAULT_CONFIGURATIONS);
        ApiInfo.IMAGES_BASE_URL = ConfigurationDefaults.IMAGES_DEFAULT_BASE_URL;
    }

    private static List<String> loadBackdropConfigurations() {
        Set<String> defaultConfigurationSet = new HashSet<>(Arrays.asList(ConfigurationDefaults.BACKDROP_DEFAULT_CONFIGURATIONS));
        Set<String> conf = sharedPreferences.getStringSet(PREF_BACKDROP_CONFIG, defaultConfigurationSet);
        return new ArrayList<>(conf);
    }

    private static List<String> loadProfileConfigurations() {
        Set<String> defaultConfigurationSet = new HashSet<>(Arrays.asList(ConfigurationDefaults.PROFILE_DEFAULT_CONFIGURATIONS));
        Set<String> configurations = sharedPreferences.getStringSet(PREF_PROFILE_CONFIG, defaultConfigurationSet);
        return new ArrayList<>(configurations);
    }

    private static List<String> loadPosterConfigurations() {
        Set<String> defaultConfigurationSet = new HashSet<>(Arrays.asList(ConfigurationDefaults.POSTER_DEFAULT_CONFIGURATIONS));
        Set<String> configurations = sharedPreferences.getStringSet(PREF_POSTER_CONFIG, defaultConfigurationSet);
        return new ArrayList<>(configurations);
    }

    public static void updateConfigurations(ConfigurationServiceProvider.Images images) {
        posterConfigurations = images.posterSizes;
        profileConfigurations = images.profileSizes;
        backdropConfigurations = images.backdropSizes;
        ApiInfo.IMAGES_BASE_URL = images.baseUrl;

        persist();
    }

    private static void persist() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(PREF_POSTER_CONFIG, new HashSet<>(posterConfigurations));
        editor.putStringSet(PREF_PROFILE_CONFIG, new HashSet<>(profileConfigurations));
        editor.putStringSet(PREF_BACKDROP_CONFIG, new HashSet<>(backdropConfigurations));
        editor.putString(PREF_SECURE_IMAGE_URL, ApiInfo.IMAGES_BASE_URL);
        editor.putLong(PREF_CONFIG_UPDATED_AT, System.currentTimeMillis());
        editor.apply();
    }

    public static List<String> getPosterConfigurations() {
        return posterConfigurations;
    }

    public static List<String> getProfileConfigurations() {
        return profileConfigurations;
    }

    public static List<String> getBackdropConfigurations() {
        return backdropConfigurations;
    }

    public static Long getLastUpdatedTime() {
        return sharedPreferences.getLong(PREF_CONFIG_UPDATED_AT, 0);
    }
}
