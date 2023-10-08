package com.weather.models;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record WeatherAPIResponse(Location location, Current current) {
    public double getTempC() {
        return current.tempC();
    }

    public LocalDateTime getDataTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(current.lastUpdated(), formatter);
    }
}
record Location(
        String name,
        String region,
        String country,
        double lat,
        double lon,
        String tz_id,
        long localtime_epoch,
        String localtime
) {
}

record Condition(
        String text,
        String icon,
        int code
) {
}

record Current(
        @JsonProperty("last_updated_epoch") long lastUpdatedEpoch,
        @JsonProperty("last_updated") String lastUpdated,
        @JsonProperty("temp_c") double tempC,
        @JsonProperty("temp_f") double tempF,
        @JsonProperty("is_day") int isDay,
        Condition condition,
        @JsonProperty("wind_mph") double windMph,
        @JsonProperty("wind_kph") double windKph,
        @JsonProperty("wind_degree") int windDegree,
        @JsonProperty("wind_dir") String windDir,
        @JsonProperty("pressure_mb") double pressureMb,
        @JsonProperty("pressure_in") double pressureIn,
        @JsonProperty("precip_mm") double precipMm,
        @JsonProperty("precip_in") double precipIn,
        int humidity,
        int cloud,
        @JsonProperty("feelslike_c") double feelslikeC,
        @JsonProperty("feelslike_f") double feelslikeF,
        @JsonProperty("vis_km") double visKm,
        @JsonProperty("vis_miles") double visMiles,
        double uv,
        @JsonProperty("gust_mph") double gustMph,
        @JsonProperty("gust_kph") double gustKph
) { }
