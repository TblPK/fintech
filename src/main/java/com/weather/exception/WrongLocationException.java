package com.weather.exception;

public class WrongLocationException extends RuntimeException {
    public WrongLocationException(String message) {
        super(message);
    }
}
