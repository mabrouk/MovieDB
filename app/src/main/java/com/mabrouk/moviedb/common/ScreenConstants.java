package com.mabrouk.moviedb.common;

/**
 * Created by VPN on 11/14/2016.
 */

public class ScreenConstants {
    private static int screenWidth = 0;
    private static int screenHeight = 0;
    private static float screenLogicalDensity = 0f;
    private static int screenDensity = 0;

    public static void init(int screenWidth, int screenHeight, int screenDensity, float screenLogicalDensity) {
        if(ScreenConstants.screenWidth > 0)
            throw new IllegalStateException("ScreenConstants shouldn't be initialized more than once");
        ScreenConstants.screenWidth = screenWidth;
        ScreenConstants.screenHeight = screenHeight;
        ScreenConstants.screenDensity = screenDensity;
        ScreenConstants.screenLogicalDensity = screenLogicalDensity;
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static float getScreenLogicalDensity() {
        return screenLogicalDensity;
    }

    public static int getScreenDensity() {
        return screenDensity;
    }
}