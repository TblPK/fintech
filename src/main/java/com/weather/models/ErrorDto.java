package com.weather.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ErrorDto (
        @JsonProperty("error") Error error
) {}

record Error (
    @JsonProperty("code") int code,
    @JsonProperty("message") String message
) {}

