package com.weather.models;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class TaskSteamAPI {

    // Рассчитать среднее значение температуры в регионах
    public static Map<String, Double> calcAvgTemperature(List<Weather> weatherList) { // CityName -> Avg Temperature
        return weatherList.stream().collect(Collectors.groupingBy(Weather::getCityName, Collectors.averagingDouble(Weather::getTemperature)));
    }

    // Создать функцию для поиска регионов, больше какой-то определенной температуры
    public static List<String> findCityWithTemperatureAbove(List<Weather> weatherList, int value) {
        return weatherList.stream().filter(w -> w.getTemperature() >= value).map(Weather::getCityName).distinct().toList();
    }

    // Преобразовать список в Map, у которой ключ - уникальный идентификатор, значение - список со значениями температур
    public static Map<UUID, List<Double>> idMap(List<Weather> weatherList) { // id -> { CityName }
        return weatherList.stream().collect(Collectors.groupingBy(Weather::getCityId, Collectors.mapping(Weather::getTemperature, Collectors.toList())));
    }

    // Преобразовать список в Map, у которой ключ - температура,
    // значение - коллекция объектов Weather, которым соответствует температура, указанная в ключе
    public static Map<Double, List<Weather>> temperatureMap(List<Weather> weatherList) { // temperature -> weather
        return weatherList.stream().collect(Collectors.groupingBy(Weather::getTemperature));
    }
}
