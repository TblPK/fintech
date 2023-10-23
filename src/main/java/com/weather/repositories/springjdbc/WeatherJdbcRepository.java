package com.weather.repositories.springjdbc;

import com.weather.models.springjdbc.Weather;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class WeatherJdbcRepository {

    private static final RowMapper<Weather> ROW_MAPPER = new BeanPropertyRowMapper<>(Weather.class);

    private static final String SQL_FIND_ALL = "SELECT * FROM weather";

    private static final String SQL_FIND_BY_ID = "SELECT * FROM weather WHERE id = :id";

    private static final String SQL_INSERT = "INSERT INTO weather (city_id, date_time, weather_type_id) VALUES (:cityId, :dateTime, :weatherTypeId)";

    private static final String SQL_UPDATE_BY_ID = "UPDATE weather SET city_id = :cityId, date_time = :dateTime, weather_type_id = :weatherTypeId WHERE id = :id";

    private static final String SQL_DELETE_BY_ID = "DELETE FROM weather WHERE id = :id";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<Weather> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, Collections.emptyMap(), ROW_MAPPER);
    }

    public Weather findById(Integer id) {
        Map<String, Object> params = Map.of("id", id);
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, params, ROW_MAPPER);

    }

    public void insert(Weather weather) {
        Map<String, Object> params = new HashMap<>();
        params.put("cityId", weather.getCityId().getId());
        params.put("dateTime", weather.getDateTime());
        params.put("weatherTypeId", weather.getWeatherType().getId());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(SQL_INSERT, new MapSqlParameterSource(params), keyHolder);
    }

    public void updateById(Weather weather) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", weather.getId());
        params.put("cityId", weather.getCityId().getId());
        params.put("dateTime", weather.getDateTime());
        params.put("weatherTypeId", weather.getWeatherType().getId());

        jdbcTemplate.update(SQL_UPDATE_BY_ID, params);
    }

    public void deleteById(Integer id) {
        Map<String, Object> params = Map.of("id", id);
        jdbcTemplate.update(SQL_DELETE_BY_ID, params);
    }
}
