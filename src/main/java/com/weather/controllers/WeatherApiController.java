package com.weather.controllers;

import com.weather.models.WeatherApiDto;
import com.weather.services.TransactionalJdbcService;
import com.weather.services.TransactionalJpaService;
import com.weather.services.WeatherAPIService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/weather-api")
public class WeatherApiController {
    private final WeatherAPIService weatherAPIService;
    private final TransactionalJpaService transactionalJpaService;
    private final TransactionalJdbcService transactionalJdbcService;

    @GetMapping("/{city}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @RateLimiter(name = "weatherAPI")
    public WeatherApiDto getWeather(@PathVariable("city") String cityName) {
        return weatherAPIService.getCurrentWeather(cityName);
    }

    @PutMapping("/jpa/{cityName}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void updateWeatherViaJpa(@PathVariable String cityName) {
        transactionalJpaService.updateWeathersByName(cityName);
    }
    @PutMapping("/jdbc/{cityName}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void updateWeatherViaJdbc(@PathVariable String cityName) {
        transactionalJdbcService.updateWeathersByName(cityName);
    }

}
