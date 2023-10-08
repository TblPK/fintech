package com.weather.exception;

public class KeyNotProvidedException extends RuntimeException {
    public KeyNotProvidedException(String message) {
        super(message);
    }
}
