package com.weather.repositories.springjdbc;

import com.weather.models.springjdbc.WeatherType;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

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

}
