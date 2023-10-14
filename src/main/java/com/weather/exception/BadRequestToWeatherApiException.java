package com.weather.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class BadRequestToWeatherApiException extends RuntimeException {
    private final HttpStatusCode httpStatusCode;

    public BadRequestToWeatherApiException(String message, HttpStatusCode httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }
}