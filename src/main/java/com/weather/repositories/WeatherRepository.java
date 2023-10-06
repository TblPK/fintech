package com.weather.repositories;

import com.weather.models.Weather;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
@Getter
public class WeatherRepository {

    private final Map<String, List<Weather>> tempDB = new HashMap<>();
    private final Map<String, UUID> uuidMap = new HashMap<>();

    {
        LocalDateTime time = LocalDateTime.now();
        addCity("Msk", 0.1, time);
        addWeather("Msk", 3.5, LocalDateTime.of(2023, 10, 5, 17, 30, 0));
        addCity("Spb", -24.5, time);
        addCity("Vlg", 33.5, time);
        addWeather("Vlg", 30.5, LocalDateTime.of(2023, 10, 5, 17, 30, 0));
    }

    public boolean addCity(String cityName, double temperature, LocalDateTime dateTime) {
        if (!tempDB.containsKey(cityName)) {
            tempDB.put(cityName, new ArrayList<>());
            uuidMap.put(cityName, UUID.randomUUID());
            Weather weather = new Weather(uuidMap.get(cityName), cityName);
            weather.setTemperature(temperature);
            weather.setDateTime(dateTime);
            tempDB.get(cityName).add(weather);
            return true;
        }
        return false;
    }

    public void addWeather(String cityName, double temperature, LocalDateTime dateTime) {
        if (tempDB.containsKey(cityName)) {
            Weather weather = new Weather(uuidMap.get(cityName), cityName);
            weather.setTemperature(temperature);
            weather.setDateTime(dateTime);
            tempDB.get(cityName).add(weather);
        }
    }
}
