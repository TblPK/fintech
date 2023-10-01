package com.weather.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@ToString
public class Weather {
    private static final Map<String, UUID> uuidMap = new HashMap<>();
    @Setter(AccessLevel.PRIVATE)
    private UUID cityId;
    private String cityName;
    private Double temperature;
    private LocalDateTime dateTime;

    public Weather(String cityName, double temperature, LocalDateTime dateTime) {
        this.temperature = temperature;
        this.dateTime = dateTime;
        setCityName(cityName);
    }

    // UUID меняется вместе с cityName
    public void setCityName(String cityName) {
        this.cityName = cityName;
        cityId = generateCityId(cityName);
    }

    private UUID generateCityId(String cityName) {
        UUID uuid = uuidMap.get(cityName);
        if (uuid == null) {
            uuid = UUID.randomUUID();
            uuidMap.put(cityName, uuid);
        }
        return uuid;
    }
}
