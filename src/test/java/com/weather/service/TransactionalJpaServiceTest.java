package com.weather.service;

import com.weather.TestContainersH2Test;
import com.weather.exception.WeatherNotFoundException;
import com.weather.models.springdatajpa.City;
import com.weather.models.springdatajpa.Weather;
import com.weather.models.springdatajpa.WeatherType;
import com.weather.repositories.springdatajpa.CityJpaRepository;
import com.weather.repositories.springdatajpa.WeatherJpaRepository;
import com.weather.repositories.springdatajpa.WeatherTypeJpaRepository;
import com.weather.services.TransactionalJpaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionalJpaServiceTest extends TestContainersH2Test {

    @Autowired
    WeatherJpaRepository weatherJpaRepository;

    @Autowired
    WeatherTypeJpaRepository weatherTypeJpaRepository;

    @Autowired
    CityJpaRepository cityJpaRepository;

    @Autowired
    TransactionalJpaService transactionalJpaService;

    String cityName = "Moscow";

    @BeforeEach
    void setup() {
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
    }

    @Test
    void updateWeatherViaJpaShouldUpdateExistsWeather() {
        Weather weather = weatherJpaRepository.findAll().get(0);
        transactionalJpaService.updateWeathersByName(cityName);
        Weather weatherUpdated = weatherJpaRepository.findAll().get(0);
        assertNotEquals(weather.getTemperature(), weatherUpdated.getTemperature());
        assertNotEquals(weather.getWeatherType(), weatherUpdated.getWeatherType());
        assertNotEquals(weather.getDateTime(), weatherUpdated.getDateTime());
        assertEquals(weather.getCityId().getName(), weatherUpdated.getCityId().getName());
    }

    @Test
    void updateWeatherViaJpaThrowWeatherNotFoundException() {
        assertThrows(WeatherNotFoundException.class, () -> transactionalJpaService.updateWeathersByName("NotCity"));
    }
}
