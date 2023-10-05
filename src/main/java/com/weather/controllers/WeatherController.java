package com.weather.controllers;

import com.weather.models.Weather;
import com.weather.services.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "WeatherDataController", description = "Weather data controller")
@Controller
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @Operation(summary = "Get a list of weather data in the specified city on a given date")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Successfully found a list of weather data in the specified city on a given date",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Weather.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "A list of weather data in the specified city on a given date was not found", content = @Content
            )
    })
    @GetMapping("/{city}")
    public List<Weather> getWeatherListForDate(
            @PathVariable("city") @Parameter(description = "City Name") String cityName,
            @RequestParam @Parameter(description = "Required date") LocalDate date
    ) {
        return weatherService.getCurrentWeather(cityName, date);
    }

    @Operation(summary = "Get a list of all weather data")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Successfully found a list of all weather data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Weather.class))
            )
    })
    @GetMapping("/")
    public ResponseEntity<List<Weather>> getAllWeather() {
        List<Weather> weathers = weatherService.getWeatherRepository().tempDB;
        return ResponseEntity.ok(weathers);
    }

    @Operation(summary = "Add a new city with temperature and date information")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Successfully added a new city",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Weather.class))}
            ),
            @ApiResponse(
                    responseCode = "409", description = "The city already exists", content = @Content
            )
    })
    @PostMapping("/{city}")
    public ResponseEntity<Weather> createWeather(
            @PathVariable("city") @Parameter(description = "City Name") String cityName,
            @RequestBody @Parameter(description = "Required weather data information, including temperature and date") Weather weather
    ) {
        weather.setCityName(cityName);
        weatherService.createWeather(weather);
        return ResponseEntity.ok(weather);
    }

    @Operation(summary = "Update or add weather data for a given date in the specified city")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Successfully updated or added weather data information",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Weather.class))}
            )
    })
    @PutMapping("/{city}")
    public ResponseEntity<Weather> updateWeather(
            @PathVariable("city") @Parameter(description = "City Name") String cityName,
            @RequestBody @Parameter(description = "Required weather data information, including temperature and date") Weather weather
    ) {
        weather.setCityName(cityName);
        weatherService.updateWeather(weather);
        return ResponseEntity.ok(weather);
    }

    @Operation(summary = "Delete all weather data information for a specific city")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Successfully deleted all weather data information for the specified city",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Weather.class))}
            )
    })
    @DeleteMapping("/{city}")
    public ResponseEntity<Void> deleteWeathersByCityName(
            @PathVariable("city") @Parameter(description = "City Name") String cityName
    ) {
        weatherService.deleteWeathersByCityName(cityName);
        return ResponseEntity.ok().build();
    }
}
