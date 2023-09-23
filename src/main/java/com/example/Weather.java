package com.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@ToString
@Getter
@Setter
@AllArgsConstructor
public class Weather {
    private int regionId;
    private int temperature; // °C
    private String regionName;
    private Date dateTime;
}