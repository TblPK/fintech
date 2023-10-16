package com.weather.controllers;

import com.weather.models.WeatherApiDto;
import com.weather.services.WeatherAPIService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/weather-api")
@Tag(name = "WeatherApiController", description = "Controller for Weather API")
public class WeatherApiController {
    private final WeatherAPIService weatherAPIService;

    @Operation(summary = "Get weather data from Weather Api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully weather data added", content = @Content)
    })
    @GetMapping("/{city}")
    @RateLimiter(name = "weatherAPI")
    public ResponseEntity<WeatherApiDto> getWeather(@PathVariable("city") String cityName) {
        return weatherAPIService.getCurrentWeather(cityName);
    }
}
