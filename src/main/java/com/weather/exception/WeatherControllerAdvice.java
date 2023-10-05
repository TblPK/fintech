package com.weather.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class WeatherControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<String> handleException(WeatherNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(WeatherAlreadyExistsException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CREATED);
    }

}