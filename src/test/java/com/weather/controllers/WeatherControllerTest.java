package com.weather.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.exception.WeatherAlreadyExistsException;
import com.weather.exception.WeatherNotFoundException;
import com.weather.models.Weather;
import com.weather.services.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherController.class)
class WeatherControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    WeatherService weatherService;

    @Test
    void getWeatherListForDateReturnWeatherListWhenDateEqual() throws Exception {
        LocalDate date = LocalDate.now();
        String city = "Moscow";
        Weather weather1 = Weather.builder().cityName(city).cityId(UUID.randomUUID()).build();
        Weather weather2 = Weather.builder().cityName(city).cityId(UUID.randomUUID()).build();
        List<Weather> expected = List.of(weather1, weather2);

        when(weatherService.getWeatherListForDate(city, date)).thenReturn(expected);

        mockMvc.perform(get("/api/weather/{city}", city).param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    void getWeatherListForDateReturnNotFound() throws Exception {
        LocalDate date = LocalDate.now();
        String city = "Moscow";

        when(weatherService.getWeatherListForDate(city, date))
                .thenThrow(new WeatherNotFoundException(city));

        mockMvc.perform(get("/api/weather/{city}", city).param("date", date.toString()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Data for " + city + " was not found."));
    }

    @Test
    void getAllWeatherReturnMapOfListOfAllWeatherData() throws Exception {
        String city1 = "Moscow";
        String city2 = "Saint-Petersburg";
        Weather weather1 = Weather.builder().cityName(city1).cityId(UUID.randomUUID()).build();
        Weather weather2 = Weather.builder().cityName(city1).cityId(UUID.randomUUID()).build();
        Weather weather3 = Weather.builder().cityName(city2).cityId(UUID.randomUUID()).build();
        List<Weather> weatherData1 = List.of(weather1, weather2);
        List<Weather> weatherData2 = List.of(weather3);
        Map<String, List<Weather>> expected = Map.of(
                city1, weatherData1,
                city2, weatherData2);

        when(weatherService.getAllWeathers())
                .thenReturn(expected);

        mockMvc.perform(get("/api/weather/"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    void createWeatherReturnStatusOk() throws Exception {
        String city = "Saint-Petersburg";
        Weather weather = Weather.builder().cityName(city).cityId(UUID.randomUUID()).temperature(24.5).build();

        doNothing().when(weatherService).createCity(any(), anyDouble(), any());

        mockMvc.perform(post("/api/weather/{city}", city)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(weather)))
                .andExpect(status().isOk());
    }

    @Test
    void createWeatherReturnStatusCreated() throws Exception {
        String city = "Saint-Petersburg";
        Weather weather = Weather.builder().cityName(city).cityId(UUID.randomUUID()).temperature(24.5).build();

        doThrow(new WeatherAlreadyExistsException(city)).when(weatherService).createCity(any(), anyDouble(), any());

        mockMvc.perform(post("/api/weather/{city}", city)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(weather)))
                .andExpect(status().isConflict())
                .andExpect(content().string("The city already exists: " + city));
    }

}
