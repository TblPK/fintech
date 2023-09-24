package com.example;

import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import java.time.LocalDateTime;

public class TaskSteamAPI {
    public static void main(String[] args) {
        List<Weather> weatherList = new ArrayList<>();
        weatherList.add(new Weather(1, 1, "SPB", LocalDateTime.now()));
        weatherList.add(new Weather(2, 3, "MSK", LocalDateTime.now()));
        weatherList.add(new Weather(2, 3, "VLG", LocalDateTime.now()));
        weatherList.add(new Weather(1, 4, "UFO", LocalDateTime.now()));

        System.out.println("Avg: " + calcAvgTemperature(weatherList));
        System.out.println("Find: " + findRegionsWithTemperatureAbove(weatherList, 3));
        System.out.println("IdMap: " + idMap(weatherList));
        System.out.println("TemperatureMap: ");
        temperatureMap(weatherList).forEach((t, w) -> System.out.println(t + " -> " + w));
    }

    // Рассчитать среднее значение температуры в регионах
    public static Double calcAvgTemperature(List<Weather> weatherList) {
        return weatherList.stream().mapToInt(Weather::getTemperature).average().orElse(0);
    }

    // Создать функцию для поиска регионов, больше какой-то определенной температуры
    public static List<String> findRegionsWithTemperatureAbove(List<Weather> weatherList, int value) {
        return weatherList.stream().filter(w -> w.getTemperature() >= value).map(Weather::getRegionName).toList();
    }

    // Преобразовать список в Map, у которой ключ - уникальный идентификатор, значение - список со значениями температур
    public static Map<Integer, List<Integer>> idMap(List<Weather> weatherList) { // id -> { regionName }
        return weatherList.stream().collect(Collectors.groupingBy(Weather::getRegionId, Collectors.mapping(Weather::getTemperature, Collectors.toList())));
    }

    // Преобразовать список в Map, у которой ключ - температура,
    // значение - коллекция объектов Weather, которым соответствует температура, указанная в ключе
    public static Map<Integer, List<Weather>> temperatureMap(List<Weather> weatherList) { // temperature -> weather
        return weatherList.stream().collect(Collectors.groupingBy(Weather::getTemperature));
    }
}
