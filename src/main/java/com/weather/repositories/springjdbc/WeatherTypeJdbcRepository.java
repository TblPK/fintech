package com.weather.repositories.springjdbc;

import com.weather.models.springjdbc.WeatherType;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class WeatherTypeJdbcRepository {

    private static final RowMapper<WeatherType> ROW_MAPPER = new BeanPropertyRowMapper<>(WeatherType.class);

    private static final String SQL_FIND_ALL = "SELECT * FROM weather_type";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM weather_type WHERE id = :id";
    private static final String SQL_INSERT = "INSERT INTO weather_type(type) VALUES (:type)";
    private static final String SQL_UPDATE_BY_ID = "UPDATE weather_type SET type = :type WHERE id = :id";
    private static final String SQL_DELETE_BY_ID = "DELETE FROM weather_type WHERE id = :id";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<WeatherType> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, Collections.emptyMap(), ROW_MAPPER);
    }

    public WeatherType findById(Integer id) {
        Map<String, Object> params = Map.of("id", id);
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, params, ROW_MAPPER);
    }

    public void insert(String type) {
        Map<String, Object> params = Collections.singletonMap("type", type);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(SQL_INSERT, new MapSqlParameterSource(params), keyHolder);
    }

    public void updateById(Integer id, String type) {
        Map<String, Object> params = Map.of("id", id, "type", type);
        jdbcTemplate.update(SQL_UPDATE_BY_ID, params);
    }

    public void deleteById(Integer id) {
        Map<String, Object> params = Map.of("id", id);
        jdbcTemplate.update(SQL_DELETE_BY_ID, params);
    }
}
