package com.alsritter.config;

import com.alsritter.proportion.WeatherWeight;
import com.alsritter.proportion.WeightInterface;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author alsritter
 * @version 1.0
 **/
@Configuration
public class WeightConfig {

    @Bean
    public WeightInterface weatherWeight() {
        return new WeatherWeight();
    }
}
