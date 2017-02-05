package com.mabrouk.moviedb.network;

import com.mabrouk.moviedb.common.ScreenConstants;
import com.mabrouk.moviedb.configurations.ConfigurationDefaults;
import com.mabrouk.moviedb.configurations.ConfigurationsStore;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by VPN on 11/13/2016.
 */

public class MediaUrlBuilderTest {
    String posterUrl = "/xfWac8MTYDxujaxgPVcRD9yZaul.jpg";
    String backdropUrl = "/tFI8VLMgSTTU38i8TIsklfqS9Nl.jpg";
    String profileUrl = "/nkrIGojQy6FNn9s5cfpiUAmLeNz.jpg";

    @BeforeClass
    public static void setup() {
        ScreenConstants.init(720, 1280, 2, 2f);
        ConfigurationsStore.initWithDefaults();
    }

    @Test
    public void testNoMediaType() {
        String actual = new MediaUrlBuilder(posterUrl).build();
        String expected = ConfigurationDefaults.IMAGES_DEFAULT_BASE_URL + "original" + posterUrl;
        assertTrue(actual.equals(expected));
    }

    @Test
    public void testPosterVerySmall() {
        String actual = new MediaUrlBuilder(posterUrl)
                .addType(MediaUrlBuilder.TYPE_POSTER)
                .addSize(70, 100)
                .build();
        String expected = ConfigurationDefaults.IMAGES_DEFAULT_BASE_URL +
                ConfigurationDefaults.POSTER_DEFAULT_CONFIGURATIONS[0] + posterUrl;
        assertTrue(actual.equals(expected));
    }

    @Test
    public void testPosterSmall() {
        String actual = new MediaUrlBuilder(posterUrl)
                .addType(MediaUrlBuilder.TYPE_POSTER)
                .addSize(150, 220)
                .build();
        String expected = ConfigurationDefaults.IMAGES_DEFAULT_BASE_URL +
                ConfigurationDefaults.POSTER_DEFAULT_CONFIGURATIONS[1] + posterUrl;
        assertTrue(actual.equals(expected));
    }

    @Test
    public void testPosterLarge() {
        String actual = new MediaUrlBuilder(posterUrl)
                .addType(MediaUrlBuilder.TYPE_POSTER)
                .addSize(450, 600)
                .build();
        String expected = ConfigurationDefaults.IMAGES_DEFAULT_BASE_URL +
                ConfigurationDefaults.POSTER_DEFAULT_CONFIGURATIONS[4] + posterUrl;
        assertTrue(actual.equals(expected));
    }

    @Test
    public void testPosterXLarge() {
        String actual = new MediaUrlBuilder(posterUrl)
                .addType(MediaUrlBuilder.TYPE_POSTER)
                .addSize(800, 1000)
                .build();
        String expected = ConfigurationDefaults.IMAGES_DEFAULT_BASE_URL +
                ConfigurationDefaults.POSTER_DEFAULT_CONFIGURATIONS[5] + posterUrl;
        assertTrue(actual.equals(expected));
    }

    @Test
    public void testBackdropExact() {
        String actual = new MediaUrlBuilder(backdropUrl)
                .addType(MediaUrlBuilder.TYPE_BACKDROP)
                .addSize(300, 200)
                .build();
        String expected = ConfigurationDefaults.IMAGES_DEFAULT_BASE_URL +
                ConfigurationDefaults.BACKDROP_DEFAULT_CONFIGURATIONS[0] + backdropUrl;
        assertTrue(actual.equals(expected));
    }

    @Test
    public void testProfileSmall() {
        String actual = new MediaUrlBuilder(profileUrl)
                .addType(MediaUrlBuilder.TYPE_PROFILE)
                .addSize(150, 300)
                .build();
        String expected = ConfigurationDefaults.IMAGES_DEFAULT_BASE_URL +
                ConfigurationDefaults.PROFILE_DEFAULT_CONFIGURATIONS[1] + profileUrl;
        assertTrue(actual.equals(expected));
    }

    @Test
    public void testProfileLarge() {
        String actual = new MediaUrlBuilder(profileUrl)
                .addType(MediaUrlBuilder.TYPE_PROFILE)
                .addSize(400, 600)
                .build();
        String expected = ConfigurationDefaults.IMAGES_DEFAULT_BASE_URL +
                ConfigurationDefaults.PROFILE_DEFAULT_CONFIGURATIONS[2] + profileUrl;
        assertTrue(actual.equals(expected));
    }
}
