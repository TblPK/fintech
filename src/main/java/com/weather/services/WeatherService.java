package com.weather.services;

import com.weather.exception.WeatherAlreadyExistsException;
import com.weather.exception.WeatherNotFoundException;
import com.weather.models.Weather;
import com.weather.repositories.WeatherRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Getter
@Service
@RequiredArgsConstructor
public class WeatherService {
    private final WeatherRepository weatherRepository;

    public List<Weather> getWeatherListForDate(String cityName, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.of(23, 59, 59));

        if (!weatherRepository.getTempDB().containsKey(cityName)) {
            throw new WeatherNotFoundException(cityName);
        }

        List<Weather> weatherList = weatherRepository.getTempDB().get(cityName).stream()
                .filter(w -> w.getDateTime().isAfter(startOfDay) && w.getDateTime().isBefore(endOfDay)).toList();

        if (weatherList.isEmpty()) {
            throw new WeatherNotFoundException(cityName);
        }
        return weatherList;
    }

    public void createCity(String cityName, double temperature, LocalDateTime dateTime) {
        boolean isExists = weatherRepository.addCity(cityName, temperature, dateTime);
        if (!isExists) {
            throw new WeatherAlreadyExistsException(cityName);
        }
    }

    public void updateWeather(String cityName, double temperature, LocalDateTime dateTime) {
        boolean isExists = weatherRepository.getTempDB().containsKey(cityName);
        if (!isExists) {
            throw new WeatherNotFoundException(cityName);
        }
        Optional<Weather> optional = weatherRepository.getTempDB().get(cityName).stream()
                .filter(w -> w.getCityName().equals(cityName) && w.getDateTime().isEqual(dateTime))
                .findFirst();

        if (optional.isEmpty()) {
            weatherRepository.addWeather(cityName, temperature, dateTime);
        } else {
            optional.get().setTemperature(temperature);
        }
    }

    public void deleteWeathersByCityName(String cityName) {
        weatherRepository.getTempDB().remove(cityName);
    }
}
