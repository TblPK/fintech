package com.weather.models.springdatajpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "weather_type")
@Getter
@Setter
@ToString
public class WeatherType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "weather_type")
    private String type;

}
