package com.weather.exception;

public class WeatherAlreadyExistsException extends RuntimeException {
    public WeatherAlreadyExistsException(String cityName) {
        super("The city already exists: " + cityName);
    }
}
