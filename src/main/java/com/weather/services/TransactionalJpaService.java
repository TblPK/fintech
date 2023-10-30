package com.weather.services;

import com.weather.exception.WeatherNotFoundException;
import com.weather.models.WeatherApiDto;
import com.weather.models.springdatajpa.City;
import com.weather.models.springdatajpa.Weather;
import com.weather.models.springdatajpa.WeatherType;
import com.weather.repositories.springdatajpa.CityJpaRepository;
import com.weather.repositories.springdatajpa.WeatherJpaRepository;
import com.weather.repositories.springdatajpa.WeatherTypeJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class TransactionalJpaService {
    private final WeatherJpaRepository weatherJpaRepository;
    private final DateTimeFormatter dateTimeFormatter;
    private final CityJpaRepository cityJpaRepository;
    private final WeatherAPIService weatherAPIService;
    private final WeatherTypeJpaRepository weatherTypeJpaRepository;

    public void updateWeathersByName(String cityName) {
        City city = cityJpaRepository.findCityByName(cityName);
        if (city == null) {
            throw new WeatherNotFoundException(cityName);
        }
        List<Weather> weathers = weatherJpaRepository.findAllByCityId(city);
        int i = 0;
        for (Weather weather : weathers) {
            // if (i++ == 1) throw new RuntimeException(); // для проверки работы транзакции
            updateWeather(weather, cityName);
        }
    }

    private void updateWeather(Weather weather, String cityName) {
        WeatherApiDto weatherApiDto = weatherAPIService.getCurrentWeather(cityName);
        weather.setTemperature(weatherApiDto.getTempC());
        weather.setDateTime(LocalDateTime.parse(weatherApiDto.getDataTime(), dateTimeFormatter));
        WeatherType weatherType = weatherTypeJpaRepository.findByType(weatherApiDto.getCondition());
        if (weatherType == null) {
            weatherType = new WeatherType();
            weatherType.setType(weatherApiDto.getCondition());
            weatherType = weatherTypeJpaRepository.save(weatherType);
        }
        weather.setWeatherType(weatherType);
        weatherJpaRepository.save(weather);
    }
}
