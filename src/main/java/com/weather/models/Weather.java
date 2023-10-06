package com.weather.models;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Weather {
    @NonNull
    private UUID cityId;
    @NonNull
    private String cityName;
    private Double temperature;
    private LocalDateTime dateTime;
}
