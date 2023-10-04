package com.weather.models;

import com.weather.dao.WeatherDao;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@Service
public class WeatherService {
    private final List<Weather> weathers;

    public WeatherService(WeatherDao weathersDao) {
        this.weathers = weathersDao.tempDB;
    }

    public List<Weather> getCurrentWeather(String cityName, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay(); // начало текущего дня
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX); // конец текущего дня

        return weathers.stream()
                .filter(w -> w.getCityName().equals(cityName) &&
                        w.getDateTime().isAfter(startOfDay) &&
                        w.getDateTime().isBefore(endOfDay)).toList();
    }

    public boolean createWeather(Weather weather) {
        Optional<Weather> optional = weathers.stream()
                .filter(w -> w.getCityName().equals(weather.getCityName()))
                .findFirst();

        if (optional.isPresent()) {
            return false;
        }
        weathers.add(weather);
        return true;
    }

    public void updateWeather(Weather weather) {
        Optional<Weather> optional = weathers.stream()
                .filter(w -> w.getCityName().equals(weather.getCityName()) &&
                        w.getDateTime().isEqual(weather.getDateTime()))
                .findFirst();

        if (optional.isEmpty()) {
            weathers.add(weather);
        } else {
            optional.get().setTemperature(weather.getTemperature());
        }
    }

    public void deleteWeathersByCityName(String cityName) {
        weathers.removeIf(w -> Objects.equals(w.getCityName(), cityName));
    }
}
