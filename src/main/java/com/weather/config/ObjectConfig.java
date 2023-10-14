package com.weather.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectConfig {

    @Bean
    public ObjectMapper mapper() {
        return new ObjectMapper();
    }

}
