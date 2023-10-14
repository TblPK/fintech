package com.weather.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.exception.BadRequestToWeatherApiException;
import com.weather.exception.ParsingJsonException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Component
public class WeatherTemplateErrorHandler implements ResponseErrorHandler {
    private final ObjectMapper objectMapper;

    public WeatherTemplateErrorHandler(@Qualifier("mapper") ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        try {
            String responseBody = new BufferedReader(new InputStreamReader(response.getBody()))
                    .lines()
                    .collect(Collectors.joining("\n"));

            Error error = objectMapper.readValue(responseBody, ErrorDto.class).error();
            int code = error.code();
            String message = error.message();
            throw new BadRequestToWeatherApiException("Code error and message: " + code + " " + message, response.getStatusCode());
        } catch (JsonProcessingException jsonException) {
            throw new ParsingJsonException("Error parsing JSON");
        }
    }
}
