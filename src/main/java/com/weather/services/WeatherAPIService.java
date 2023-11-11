package com.weather.services;

import com.weather.models.Properties;
import com.weather.models.WeatherApiDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherAPIService {

    private final RestTemplate weatherTemplate;
    private final Properties properties;

    public WeatherAPIService(@Qualifier("weatherTemplate") RestTemplate weatherTemplate, Properties properties) {
        this.weatherTemplate = weatherTemplate;
        this.properties = properties;
    }

    public WeatherApiDto getCurrentWeather(String cityName) {
        return weatherTemplate.exchange(
                "/current.json?q={cityName}&key={key}",
                HttpMethod.GET,
                null,
                WeatherApiDto.class,
                cityName, properties.apiKey()
        ).getBody();
    }
}