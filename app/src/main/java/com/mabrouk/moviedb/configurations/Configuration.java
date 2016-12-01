package com.mabrouk.moviedb.configurations;

/**
 * Created by VPN on 11/13/2016.
 */

public class Configuration {
    static final int DIMENSION_WIDTH = 1;
    static final int DIMENSION_HEIGHT = 2;

    int dimension;
    int value;
    String pathValue;

    public Configuration(String configurationString) {
        pathValue = configurationString;
        value = Integer.parseInt(configurationString.substring(1));
        dimension = configurationString.charAt(0) == 'w' ? DIMENSION_WIDTH : DIMENSION_HEIGHT;
    }

    /**
     * Calculates a ratio indicating how much does the current configuration match the specified size, ranging between 0 and 1.
     * Only one dimension used based on the configuration itself
     *
     * @param width
     * @param height
     * @return the matching ratio between 1 (exactly matches) and 0 (not the appropriate configuration for the specified size)
     */
    public float fitsSize(int width, int height) {
        return fitSize(dimension == DIMENSION_WIDTH ? width : height);
    }

    private float fitSize(int sizeValue) {
        int higher, lower;
        if (value < sizeValue) {
            higher = sizeValue;
            lower = value;
        } else {
            higher = value;
            lower = sizeValue;
        }

        int divisor = higher/ lower;
        if (divisor > 1)
            return 0;
        int mod = higher % lower;
        float rate = (float) mod / (float) lower;
        return 1f - rate;
    }

    public String getPathValue() {
        return pathValue;
    }
}
