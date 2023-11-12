package com.weather.services;

import com.weather.exception.WeatherNotFoundException;
import com.weather.models.WeatherApiDto;
import com.weather.models.springjdbc.City;
import com.weather.models.springjdbc.Weather;
import com.weather.models.springjdbc.WeatherType;
import com.weather.repositories.springjdbc.CityJdbcRepository;
import com.weather.repositories.springjdbc.WeatherJdbcRepository;
import com.weather.repositories.springjdbc.WeatherTypeJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionalJdbcService {
    private final WeatherAPIService weatherAPIService;
    private final DateTimeFormatter dateTimeFormatter;
    private final CityJdbcRepository cityJdbcRepository;
    private final WeatherJdbcRepository weatherJdbcRepository;
    private final WeatherTypeJdbcRepository weatherTypeJdbcRepository;
    private final Connection connection;

    public void updateWeathersByName(String cityName) {
        try {
            connection.setAutoCommit(false);
            City city = cityJdbcRepository.findCityByName(cityName);
            if (city == null) {
                throw new WeatherNotFoundException(cityName);
            }
            List<Weather> weathers = weatherJdbcRepository.findAllByCityId(city);
            int i = 0;
            for (Weather weather : weathers) {
                if (i++ == 1) throw new SQLException(); // для проверки работы транзакции
                updateWeather(weather, cityName);
            }
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void updateWeather(Weather weather, String cityName) {
        WeatherApiDto weatherApiDto = weatherAPIService.getCurrentWeather(cityName);
        weather.setTemperature(weatherApiDto.getTempC());
        weather.setDateTime(LocalDateTime.parse(weatherApiDto.getDataTime(), dateTimeFormatter));
        WeatherType weatherType = weatherTypeJdbcRepository.findByType(weatherApiDto.getCondition());
        if (weatherType == null) {
            weatherType = new WeatherType();
            weatherType.setType(weatherApiDto.getCondition());
            weatherType = weatherTypeJdbcRepository.save(weatherType);
        }
        weather.setWeatherType(weatherType);
        weatherJdbcRepository.save(weather);
    }

}
