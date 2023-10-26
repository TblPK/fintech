package com.weather.services;

import com.weather.exception.WeatherNotFoundException;
import com.weather.models.WeatherApiDto;
import com.weather.models.springjdbc.City;
import com.weather.models.springjdbc.Weather;
import com.weather.repositories.springjdbc.CityJdbcRepository;
import com.weather.repositories.springjdbc.WeatherJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TransactionalJdbcService {
    private final TransactionTemplate transactionTemplate;
    private final WeatherAPIService weatherAPIService;
    private final DateTimeFormatter dateTimeFormatter;
    private final CityJdbcRepository cityJdbcRepository;
    private final WeatherJdbcRepository weatherJdbcRepository;

    public void updateWeathersByName(String cityName) {
        transactionTemplate.executeWithoutResult(status -> {
            City city = cityJdbcRepository.findCityByName(cityName);
            if (city == null) {
                throw new WeatherNotFoundException(cityName);
            }
            List<Weather> weathers = weatherJdbcRepository.findAllByCityId(city);
            weathers.forEach(System.out::println);
            int i = 0;
            for (Weather weather : weathers) {
                //if (i++ == 1) throw new RuntimeException(); // костыль для проверки работы транзакции
                updateWeather(weather, cityName);
            }
        });
    }

    private void updateWeather(Weather weather, String cityName) {
        WeatherApiDto weatherApiDto = weatherAPIService.getCurrentWeather(cityName);
        weather.setTemperature(weatherApiDto.getTempC());
        weather.setDateTime(LocalDateTime.parse(weatherApiDto.getDataTime(), dateTimeFormatter));
        weatherJdbcRepository.save(weather);
    }

}
