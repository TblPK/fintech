package com.weather.exception;

public class ParsingJsonException extends RuntimeException {
    public ParsingJsonException(String message) {
        super(message);
    }
}
