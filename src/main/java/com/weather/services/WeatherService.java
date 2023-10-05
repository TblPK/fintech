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
import java.util.Objects;
import java.util.Optional;

@Getter
@Service
@RequiredArgsConstructor
public class WeatherService {
    private final WeatherRepository weatherRepository;

    public List<Weather> getCurrentWeather(String cityName, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.of(23, 59, 59));

        List<Weather> weatherList = weatherRepository.tempDB.stream()
                .filter(w -> w.getCityName().equals(cityName) &&
                        w.getDateTime().isAfter(startOfDay) &&
                        w.getDateTime().isBefore(endOfDay)).toList();

        if (weatherList.isEmpty()) {
            throw new WeatherNotFoundException(cityName, startOfDay, endOfDay);
        }
        return weatherList;
    }

    public void createWeather(Weather weather) {
        Optional<Weather> optional = weatherRepository.tempDB.stream()
                .filter(w -> w.getCityName().equals(weather.getCityName()))
                .findFirst();

        if (optional.isPresent()) {
            throw new WeatherAlreadyExistsException(weather.getCityName());
        }
        weatherRepository.tempDB.add(weather);
    }

    public void updateWeather(Weather weather) {
        Optional<Weather> optional = weatherRepository.tempDB.stream()
                .filter(w -> w.getCityName().equals(weather.getCityName()) &&
                        w.getDateTime().isEqual(weather.getDateTime()))
                .findFirst();

        if (optional.isEmpty()) {
            weatherRepository.tempDB.add(weather);
        } else {
            optional.get().setTemperature(weather.getTemperature());
        }
    }

    public void deleteWeathersByCityName(String cityName) {
        weatherRepository.tempDB.removeIf(w -> Objects.equals(w.getCityName(), cityName));
    }
}
