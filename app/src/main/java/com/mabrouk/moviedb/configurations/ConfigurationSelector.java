package com.mabrouk.moviedb.configurations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VPN on 11/13/2016.
 */

public class ConfigurationSelector {
    List<Configuration> configurations;

    public ConfigurationSelector(List<String> rawConfigs) {
        int size = rawConfigs.size() - 1;   //ignoring the last item, which is "original"
        configurations = new ArrayList<>(size);

        for(int i = 0; i <  size; i++) {
            String string = rawConfigs.get(i);
            configurations.add(new Configuration(string));
        }
    }

    /**
     * Selects from the type configurations the most appropriate one for the specified size
     *
     * @param width
     * @param height
     * @return the configuration path value
     */
    public String configPathForSize(int width, int height) {
        float prevRating = -1;
        int size = configurations.size();

        for(int i = 0; i < size; i++) {
            Configuration config = configurations.get(i);
            float configRating = config.fitsSize(width, height);
            if(configRating < prevRating) {     //local maxima
                return configurations.get(i - 1).getPathValue();
            }
            prevRating = configRating;
        }

        return configurations.get(size - 1).getPathValue();
    }
}
