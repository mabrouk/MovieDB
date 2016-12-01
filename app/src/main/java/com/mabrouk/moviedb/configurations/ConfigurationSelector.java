package com.mabrouk.moviedb.configurations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by VPN on 11/13/2016.
 */

public class ConfigurationSelector {
    private List<Configuration> configurations;

    public ConfigurationSelector(List<String> rawConfigs) {
        int size = rawConfigs.size();
        configurations = new ArrayList<>(size - 1);

        for(int i = 0; i <  size; i++) {
            String string = rawConfigs.get(i);
            if(string.equals("original"))
                continue;
            configurations.add(new Configuration(string));
        }
        Collections.sort(configurations, (c1, c2) -> {
            if(c1.value < c2.value)
                return -1;
            else if(c1.value < c2.value)
                return 1;
            else return 0;
        });
        System.out.print("");
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
