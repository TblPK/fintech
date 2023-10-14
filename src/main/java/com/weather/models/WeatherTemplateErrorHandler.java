package com.weather.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.exception.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Component
public class WeatherTemplateErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        JsonNode error = new ObjectMapper().readTree(response.getBody()).get("error");
        String message = error.get("message").asText();

        switch (error.get("code").asInt()) {
            case 1002 -> throw new KeyNotProvidedException(message);
            case 1003 -> throw new ParameterNotProvidedException(message);
            case 1005 -> throw new InvalidUrlException(message);
            case 1006 -> throw new WrongLocationException(message);
            // more exceptions
            default -> throw new UnknownWeatherApiException(message);
        }

    }

}
