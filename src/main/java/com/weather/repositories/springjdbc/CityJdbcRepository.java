package com.weather.repositories.springjdbc;

import com.weather.models.springjdbc.City;
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
public class CityJdbcRepository {

    private static final RowMapper<City> ROW_MAPPER = new BeanPropertyRowMapper<>(City.class);

    private static final String SQL_FIND_ALL = "SELECT * FROM city";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM city WHERE id = :id";
    private static final String SQL_INSERT = "INSERT INTO city(name) VALUES (:name)";
    private static final String SQL_UPDATE_BY_ID = "UPDATE city SET name = :name WHERE id = :id";
    private static final String SQL_DELETE_BY_ID = "DELETE FROM city WHERE id = :id";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<City> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, Collections.emptyMap(), ROW_MAPPER);
    }

    public City findById(Integer id) {
        Map<String, Object> params = Map.of("id", id);
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, params, ROW_MAPPER);
    }

    public void insert(String name) {
        Map<String, Object> params = Collections.singletonMap("name", name);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(SQL_INSERT, new MapSqlParameterSource(params), keyHolder);
    }

    public void updateById(Integer id, String name) {
        Map<String, Object> params = Map.of("id", id, "name", name);
        jdbcTemplate.update(SQL_UPDATE_BY_ID, params);
    }

    public void deleteById(Integer id) {
        Map<String, Object> params = Map.of("id", id);
        jdbcTemplate.update(SQL_DELETE_BY_ID, params);
    }
}
