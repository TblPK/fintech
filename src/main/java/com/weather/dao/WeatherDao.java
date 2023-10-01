package com.weather.dao;

import com.weather.models.Weather;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class WeatherDao {

    public final List<Weather> tempDB = new ArrayList<>();

    {
        LocalDateTime time = LocalDateTime.now();
        tempDB.add(new Weather("Msk", 0.1, time));
        tempDB.add(new Weather("Msk", 3.5, LocalDateTime.of(2023,10, 1,17,30,0)));
        tempDB.add(new Weather("Spb", -24.5, time));
        tempDB.add(new Weather("Vlg", 33.5, time));
        tempDB.add(new Weather("Vlg", 30.5, LocalDateTime.of(2023,10, 1,17,30,0)));
    }
}
