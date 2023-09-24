package com.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@AllArgsConstructor
public class Weather {
    private int regionId;
    private double temperature; // °C
    private String regionName;
    private LocalDateTime dateTime;
}