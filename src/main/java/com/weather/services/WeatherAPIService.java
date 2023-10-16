package com.weather.services;

import com.weather.models.WeatherApiDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class WeatherAPIService {

    private final RestTemplate weatherTemplate;
    private final WeatherService weatherService;
    private final DateTimeFormatter formatter;

    public WeatherAPIService(
            @Qualifier("weatherTemplate") RestTemplate weatherTemplate,
            WeatherService weatherService,
            DateTimeFormatter formatter
    ) {
        this.weatherTemplate = weatherTemplate;
        this.weatherService = weatherService;
        this.formatter = formatter;
    }

    public ResponseEntity<WeatherApiDto> getCurrentWeather(String cityName) {
        ResponseEntity<WeatherApiDto> response = weatherTemplate.exchange(
                "/current.json?q={cityName}",
                HttpMethod.GET,
                null,
                WeatherApiDto.class,
                cityName
        );

        double temperature = response.getBody().getTempC();
        LocalDateTime dataTime = LocalDateTime.parse(response.getBody().getDataTime(), formatter);
        weatherService.updateWeather(cityName, temperature, dataTime);

        return response;
    }
}