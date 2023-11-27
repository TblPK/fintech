package com.weather.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.TestContainersH2Test;
import com.weather.models.WeatherApiDto;
import com.weather.models.springdatajpa.City;
import com.weather.models.springdatajpa.Weather;
import com.weather.models.springdatajpa.WeatherType;
import com.weather.repositories.springdatajpa.CityJpaRepository;
import com.weather.repositories.springdatajpa.WeatherJpaRepository;
import com.weather.repositories.springdatajpa.WeatherTypeJpaRepository;
import com.weather.services.WeatherAPIService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class WeatherAPIControllerTest extends TestContainersH2Test {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WeatherJpaRepository weatherJpaRepository;

    @Autowired
    WeatherTypeJpaRepository weatherTypeJpaRepository;

    @Autowired
    CityJpaRepository cityJpaRepository;

    @MockBean
    WeatherAPIService weatherAPIService;

    String cityName = "Moscow";
    String NotCity = "NotCity";

    @BeforeEach
    void setup() throws JsonProcessingException {
        weatherJpaRepository.deleteAll();
        weatherTypeJpaRepository.deleteAll();
        cityJpaRepository.deleteAll();

        WeatherType weatherType = new WeatherType();
        weatherType.setType("NotType");
        weatherTypeJpaRepository.save(weatherType);

        City city = new City();
        city.setName(cityName);
        cityJpaRepository.save(city);

        Weather weather = new Weather();
        weather.setDateTime(LocalDateTime.MIN);
        weather.setTemperature(-100.0);
        weather.setWeatherType(weatherType);
        weather.setCityId(city);
        weatherJpaRepository.save(weather);
        when(weatherAPIService.getCurrentWeather(anyString())).thenReturn(response());
    }

    @Test
    void updateWeatherViaJpaShouldUpdateExistsWeather() throws Exception {
        Weather weather = weatherJpaRepository.findAll().get(0);

        mockMvc.perform(put("/weather-api/jpa/{city}", cityName))
                .andExpect(status().isOk());

        Weather weatherUpdated = weatherJpaRepository.findAll().get(0);
        assertNotEquals(weather.getTemperature(), weatherUpdated.getTemperature());
        assertNotEquals(weather.getWeatherType(), weatherUpdated.getWeatherType());
        assertNotEquals(weather.getDateTime(), weatherUpdated.getDateTime());
        assertEquals(weather.getCityId().getName(), weatherUpdated.getCityId().getName());
    }

    @Test
    void updateWeatherViaJpaThrowWeatherNotFoundException() throws Exception {
        mockMvc.perform(put("/weather-api/jpa/{city}", NotCity))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Data for " + NotCity + " was not found."));
    }

    @Test
    void updateWeatherViaJdbcShouldUpdateExistsWeather() throws Exception{
        Weather weather = weatherJpaRepository.findAll().get(0);

        mockMvc.perform(put("/weather-api/jdbc/{city}", cityName))
                .andExpect(status().isOk());

        Weather weatherUpdated = weatherJpaRepository.findAll().get(0);
        assertNotEquals(weather.getTemperature(), weatherUpdated.getTemperature());
        assertNotEquals(weather.getWeatherType(), weatherUpdated.getWeatherType());
        assertNotEquals(weather.getDateTime(), weatherUpdated.getDateTime());
        assertEquals(weather.getCityId().getName(), weatherUpdated.getCityId().getName());
    }

    @Test
    void updateWeatherViaJdbcThrowWeatherNotFoundException() throws Exception {
        mockMvc.perform(put("/weather-api/jdbc/{city}", "NotCity"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Data for " + NotCity + " was not found."));
    }

    WeatherApiDto response() throws JsonProcessingException {
        String value = "{\"location\":{\"name\":\"Moscow\",\"region\":\"Moscow City\",\"country\":\"Russia\",\"lat\":55.75,\"lon\":37.62,\"tz_id\":\"Europe/Moscow\",\"localtime\":\"2023-11-11 18:46\"},\"current\":{\"last_updated_epoch\":1699717500,\"last_updated\":\"2023-11-11 18:45\",\"temp_c\":4.0,\"temp_f\":39.2,\"is_day\":0,\"condition\":{\"text\":\"Partly cloudy\",\"icon\":\"//cdn.weatherapi.com/weather/64x64/night/116.png\",\"code\":1003},\"wind_mph\":11.9,\"wind_kph\":19.1,\"wind_degree\":150,\"wind_dir\":\"SSE\",\"pressure_mb\":1014.0,\"pressure_in\":29.94,\"precip_mm\":0.0,\"precip_in\":0.0,\"humidity\":87,\"cloud\":25,\"feelslike_c\":0.7,\"feelslike_f\":33.2,\"vis_km\":10.0,\"vis_miles\":6.0,\"uv\":1.0,\"gust_mph\":13.4,\"gust_kph\":21.6}}";
        return objectMapper.readValue(value, WeatherApiDto.class);
    }
}
