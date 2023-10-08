package com.weather.exception;

public class UnknownWeatherApiException extends RuntimeException {
    public UnknownWeatherApiException(String message) {
        super(message);
    }
}
