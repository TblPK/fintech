package com.weather.models;


import com.fasterxml.jackson.annotation.JsonProperty;

public record WeatherApiDto(
        @JsonProperty("location") Location location,
        @JsonProperty("current") Current current
) {
    public double getTempC() {
        return current.tempC();
    }

    public String getDataTime() {
        return current.lastUpdated();
    }

    public String getCondition() {
        return current.condition().text();
    }
}

record Location(
        @JsonProperty("name") String name,
        @JsonProperty("region") String region,
        @JsonProperty("country") String country,
        @JsonProperty("lat") double lat,
        @JsonProperty("lon") double lon,
        @JsonProperty("tz_id") String tz_id,
        @JsonProperty("localtime") String localtime
) { }

record Condition(
        @JsonProperty("text") String text,
        @JsonProperty("icon") String icon,
        @JsonProperty("code") int code
) { }

record Current(
        @JsonProperty("last_updated_epoch") long lastUpdatedEpoch,
        @JsonProperty("last_updated") String lastUpdated,
        @JsonProperty("temp_c") double tempC,
        @JsonProperty("temp_f") double tempF,
        @JsonProperty("is_day") int isDay,
        @JsonProperty("condition") Condition condition,
        @JsonProperty("wind_mph") double windMph,
        @JsonProperty("wind_kph") double windKph,
        @JsonProperty("wind_degree") int windDegree,
        @JsonProperty("wind_dir") String windDir,
        @JsonProperty("pressure_mb") double pressureMb,
        @JsonProperty("pressure_in") double pressureIn,
        @JsonProperty("precip_mm") double precipMm,
        @JsonProperty("precip_in") double precipIn,
        @JsonProperty("humidity") int humidity,
        @JsonProperty("cloud") int cloud,
        @JsonProperty("feelslike_c") double feelslikeC,
        @JsonProperty("feelslike_f") double feelslikeF,
        @JsonProperty("vis_km") double visKm,
        @JsonProperty("vis_miles") double visMiles,
        @JsonProperty("uv") double uv,
        @JsonProperty("gust_mph") double gustMph,
        @JsonProperty("gust_kph") double gustKph
) { }
