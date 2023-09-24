package com.example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskSteamAPI {
    public static void main(String[] args) {
        List<Weather> weatherList = new ArrayList<>();
        weatherList.add(new Weather(1, 1.0, "SPB", LocalDateTime.now()));
        weatherList.add(new Weather(1, 2.0, "SPB", LocalDateTime.now()));
        weatherList.add(new Weather(2, 2.0, "MSK", LocalDateTime.now()));
        weatherList.add(new Weather(3, 4.0, "VLG", LocalDateTime.now()));

        System.out.println("Avg: " + calcAvgTemperature(weatherList));
        System.out.println("Find: " + findRegionsWithTemperatureAbove(weatherList, 1));
        System.out.println("IdMap: " + idMap(weatherList));
        System.out.println("TemperatureMap: ");
        temperatureMap(weatherList).forEach((t, w) -> System.out.println(t + " -> " + w));
    }

    // Рассчитать среднее значение температуры в регионах
    public static Map<String, Double> calcAvgTemperature(List<Weather> weatherList) { // RegionName -> Avg Temperature
        return weatherList.stream().collect(Collectors.groupingBy(Weather::getRegionName, Collectors.averagingDouble(Weather::getTemperature)));
    }

    // Создать функцию для поиска регионов, больше какой-то определенной температуры
    public static List<String> findRegionsWithTemperatureAbove(List<Weather> weatherList, int value) {
        return weatherList.stream().filter(w -> w.getTemperature() >= value).map(Weather::getRegionName).distinct().toList();
    }

    // Преобразовать список в Map, у которой ключ - уникальный идентификатор, значение - список со значениями температур
    public static Map<Integer, List<Double>> idMap(List<Weather> weatherList) { // id -> { regionName }
        return weatherList.stream().collect(Collectors.groupingBy(Weather::getRegionId, Collectors.mapping(Weather::getTemperature, Collectors.toList())));
    }

    // Преобразовать список в Map, у которой ключ - температура,
    // значение - коллекция объектов Weather, которым соответствует температура, указанная в ключе
    public static Map<Double, List<Weather>> temperatureMap(List<Weather> weatherList) { // temperature -> weather
        return weatherList.stream().collect(Collectors.groupingBy(Weather::getTemperature));
    }
}
