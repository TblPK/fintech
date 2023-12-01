package com.weather.controllers;

import com.weather.models.Weather;
import com.weather.services.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Tag(name = "WeatherDataController", description = "Weather data controller")
@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    /**
     * Получение списка данных о погоде в указанном городе на заданную дату.
     *
     * @param cityName Название города.
     * @param date     Заданная дата.
     * @return Список данных о погоде.
     */
    @Operation(summary = "Get a list of weather data in the specified city on a given date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found a list of weather data in the specified city on a given date", content = @Content),
            @ApiResponse(responseCode = "404", description = "A list of weather data in the specified city on a given date was not found", content = @Content)
    })
    @GetMapping("/{city}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<Weather> getWeatherListForDate(
            @PathVariable("city") @Parameter(description = "City Name") String cityName,
            @RequestParam @Parameter(description = "Required date") LocalDate date
    ) {
        return weatherService.getWeatherListForDate(cityName, date);
    }

    /**
     * Получение списка всех данных о погоде во всех городах.
     *
     * @return Карта с данными о погоде.
     */
    @Operation(summary = "Get a list of all weather data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found a list of all weather data", content = @Content)
    })
    @GetMapping("/")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Map<String, List<Weather>> getAllWeather() {
        return weatherService.getAllWeathers();
    }

    /**
     * Добавление нового города с информацией о температуре и дате.
     *
     * @param cityName Название города.
     * @param weather  Информация о погоде, включая температуру и дату.
     */
    @Operation(summary = "Add a new city with temperature and date information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added a new city", content = @Content),
            @ApiResponse(responseCode = "409", description = "The city already exists", content = @Content)
    })
    @PostMapping("/{city}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void createWeather(
            @PathVariable("city") @Parameter(description = "City Name") String cityName,
            @RequestBody @Parameter(description = "Required weather data information, including temperature and date") Weather weather
    ) {
        weatherService.createCity(cityName, weather.getTemperature(), weather.getDateTime());
    }

    /**
     * Обновление или добавление данных о погоде на заданную дату в указанном городе.
     *
     * @param cityName Название города.
     * @param weather  Информация о погоде, включая температуру и дату.
     */
    @Operation(summary = "Update or add weather data for a given date in the specified city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated or added weather data information", content = @Content),
            @ApiResponse(responseCode = "404", description = "The weather already exists", content = @Content)
    })
    @PutMapping("/{city}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void updateWeather(
            @PathVariable("city") @Parameter(description = "City Name") String cityName,
            @RequestBody @Parameter(description = "Required weather data information, including temperature and date") Weather weather
    ) {
        weatherService.updateWeather(cityName, weather.getTemperature(), weather.getDateTime());
    }

    /**
     * Удаление всех данных о погоде для указанного города.
     *
     * @param cityName Название города.
     */
    @Operation(summary = "Delete all weather data information for a specific city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted all weather data information for the specified city", content = @Content)
    })
    @DeleteMapping("/{city}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void deleteWeathersByCityName(
            @PathVariable("city") @Parameter(description = "City Name") String cityName
    ) {
        weatherService.deleteWeathersByCityName(cityName);
    }
}
