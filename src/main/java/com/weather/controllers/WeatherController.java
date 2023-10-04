package com.weather.controllers;

import com.weather.models.Weather;
import com.weather.models.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Название контроллера", description = "Описание контролера")
@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Operation(summary = "Get the current temperature in the specified city for the given date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found the temperature in the city on the specified date",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Weather.class))),
            @ApiResponse(responseCode = "404", description = "Information is not found", content = @Content)})
    @GetMapping("/{city}")
    public ResponseEntity<List<Weather>> getCurrentWeather(@PathVariable("city") @Parameter(description = "City Name") String cityName,
                                                           @RequestParam @Parameter(description = "Required date") LocalDate date) {
        List<Weather> weathers = weatherService.getCurrentWeather(cityName, date);
        if (weathers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(weathers);
    }

    @Operation(summary = "Retrieve a list of all weather records")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieve a list of all weather records",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Weather.class))),
            @ApiResponse(responseCode = "404", description = "Information is not found", content = @Content)})
    @GetMapping("/")
    public ResponseEntity<List<Weather>> getAllWeather() {
        List<Weather> weathers = weatherService.getWeathers();
        if (weathers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(weathers);
    }

    @Operation(summary = "Add a new city with temperature and date information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added a new city",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Weather.class))}),
            @ApiResponse(responseCode = "404", description = "The weather already exists", content = @Content)})
    @PostMapping("/{city}")
    public ResponseEntity<Weather> createWeather(@PathVariable("city") @Parameter(description = "Name of the city") String cityName,
                                                 @RequestBody @Parameter(description = "Weather information including temperature and date") Weather weather) {
        weather.setCityName(cityName);
        boolean isCreated = weatherService.createWeather(weather);
        if (isCreated) {
            return ResponseEntity.ok(weather);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Update or add weather for a given date in the specified city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated or added weather information",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Weather.class))})})
    @PutMapping("/{city}")
    public ResponseEntity<Weather> updateWeather(@PathVariable("city") @Parameter(description = "Name of the city") String cityName,
                                                         @RequestBody @Parameter(description = "Weather information including temperature and date") Weather weather) {
        weather.setCityName(cityName);
        weatherService.updateWeather(weather);
        return ResponseEntity.ok(weather);
    }

    @Operation(summary = "Delete all weather information for a specific city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted all weather information for the specified city",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Weather.class))})})
    @DeleteMapping("/{city}")
    public ResponseEntity<Void> deleteWeathersByCityName(@PathVariable("city") @Parameter(description = "Name of the city") String cityName) {
        weatherService.deleteWeathersByCityName(cityName);
        return ResponseEntity.ok().build();
    }
}
