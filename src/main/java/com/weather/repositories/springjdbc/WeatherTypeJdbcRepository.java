package com.weather.repositories.springjdbc;

import com.weather.models.springjdbc.WeatherType;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class WeatherTypeJdbcRepository {

    private static final RowMapper<WeatherType> ROW_MAPPER = (rs, rowNum) -> {
        WeatherType weatherType = new WeatherType();
        weatherType.setId(rs.getInt("id"));
        weatherType.setType(rs.getString("type"));
        return weatherType;
    };

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public WeatherType findByType(String weatherType) {
        Map<String, Object> params = Map.of("type", weatherType);
        List<WeatherType> result = jdbcTemplate.query("SELECT * FROM weather_type WHERE type = :type", params, ROW_MAPPER);
        return result.isEmpty()? null : result.get(0);
    }

    public WeatherType save(WeatherType weatherType) {
        Map<String, Object> params = Map.of("type", weatherType.getType());
        jdbcTemplate.update("INSERT INTO weather_type(type) VALUES (:type);", params);
        return findByType(weatherType.getType());
    }

}
