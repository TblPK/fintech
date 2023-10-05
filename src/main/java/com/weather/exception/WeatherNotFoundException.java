package com.weather.exception;

public class WeatherNotFoundException extends RuntimeException {
    public WeatherNotFoundException(String cityName) {
        super("Data for " + cityName + " was not found.");
    }
}
