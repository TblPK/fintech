package com.weather.repositories.springjdbc;

import com.weather.models.springjdbc.WeatherType;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
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

    public void insert(WeatherType weatherType) {
        Map<String, Object> params = Collections.singletonMap("type", weatherType.getType());
        jdbcTemplate.update(SQL_INSERT, params);
    }

    public void updateById(WeatherType weatherType) {
        Map<String, Object> params = Map.of("id", weatherType.getId(), "type", weatherType.getType());
        jdbcTemplate.update(SQL_UPDATE_BY_ID, params);
    }

    public void deleteById(Integer id) {
        Map<String, Object> params = Map.of("id", id);
        jdbcTemplate.update(SQL_DELETE_BY_ID, params);
    }
}
