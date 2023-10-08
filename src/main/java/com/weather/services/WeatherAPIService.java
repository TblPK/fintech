package com.weather.services;

import com.weather.models.WeatherAPIResponse;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
public class WeatherAPIService {

    @Qualifier("weatherTemplate")
    private final RestTemplate weatherTemplate;
    private final WeatherService weatherService;

    public WeatherAPIService(RestTemplate weatherTemplate, WeatherService weatherService) {
        this.weatherTemplate = weatherTemplate;
        this.weatherService = weatherService;
    }

    @RateLimiter(name = "weatherAPI")
    public ResponseEntity<WeatherAPIResponse> getCurrentWeatherFromWeatherAPI(String cityName) {
        ResponseEntity<WeatherAPIResponse> response = weatherTemplate.exchange(
                "/current.json?q={cityName}",
                HttpMethod.GET,
                null,
                WeatherAPIResponse.class,
                cityName
        );

        double temperature = response.getBody().getTempC();
        LocalDateTime dataTime = response.getBody().getDataTime();
        weatherService.updateWeather(cityName, temperature, dataTime);

        return response;
    }
}