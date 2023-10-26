package com.weather.repositories.springjdbc;

import com.weather.models.springjdbc.City;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CityJdbcRepository {

    private static final RowMapper<City> ROW_MAPPER = (rs, rowNum) -> {
        City city = new City();
        city.setId(rs.getInt("id"));
        city.setName(rs.getString("name"));
        return city;
    };

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public City findCityByName(String name) {
        Map<String, Object> params = Map.of("name", name);
        return jdbcTemplate.queryForObject("SELECT * FROM city WHERE name = :name", params, ROW_MAPPER);
    }
}