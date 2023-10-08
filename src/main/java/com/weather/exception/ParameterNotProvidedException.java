package com.weather.exception;

public class ParameterNotProvidedException extends RuntimeException {
    public ParameterNotProvidedException(String message) {
        super(message);
    }
}
