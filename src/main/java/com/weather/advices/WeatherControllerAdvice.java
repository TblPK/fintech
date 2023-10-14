package com.weather.advices;

import com.weather.exception.BadRequestToWeatherApiException;
import com.weather.exception.ParsingJsonException;
import com.weather.exception.WeatherAlreadyExistsException;
import com.weather.exception.WeatherNotFoundException;
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
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(ParsingJsonException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(BadRequestToWeatherApiException exception) {
        return new ResponseEntity<>(exception.getMessage(), exception.getHttpStatusCode());
    }
}