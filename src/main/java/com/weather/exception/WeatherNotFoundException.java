package com.weather.exception;

import java.time.LocalDateTime;

public class WeatherNotFoundException extends RuntimeException {
    public WeatherNotFoundException(String cityName, LocalDateTime startOfDay, LocalDateTime endOfDay) {
        super("Data for the region " + cityName + " for the period from " + startOfDay + " to " + endOfDay + " not found.");
    }
}
