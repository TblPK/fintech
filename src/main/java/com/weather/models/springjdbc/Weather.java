package com.weather.models.springjdbc;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Weather {
    private Integer id;
    private City cityId;
    private LocalDateTime dateTime;
    private Double temperature;
    private WeatherType weatherType;
}
