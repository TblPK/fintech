package com.example;

import lombok.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ToString
@Getter
@Setter
@RequiredArgsConstructor
public class Weather {
    private static Map<String, UUID> uuidMap = new HashMap<>();
    @NonNull
    private double temperature; // °C
    @NonNull
    private String regionName;
    @NonNull
    private LocalDateTime dateTime;
    private final UUID regionId = generateRegionId(regionName);

    private static UUID generateRegionId(String regionName) {
        UUID uuid = uuidMap.get(regionName);
        if (uuid == null) {
            uuid = UUID.randomUUID();
            uuidMap.put(regionName, uuid);
        }
        return uuid;
    }
}