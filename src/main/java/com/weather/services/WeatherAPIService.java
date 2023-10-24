package com.weather.services;

import com.weather.models.WeatherApiDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherAPIService {

    private final RestTemplate weatherTemplate;
    public WeatherAPIService(@Qualifier("weatherTemplate") RestTemplate weatherTemplate) {
        this.weatherTemplate = weatherTemplate;
    }

    public WeatherApiDto getCurrentWeather(String cityName) {
        return weatherTemplate.exchange(
                "/current.json?q={cityName}",
                HttpMethod.GET,
                null,
                WeatherApiDto.class,
                cityName
        ).getBody();
    }
}