package com.weather.repositories.springjdbc;

import com.weather.models.springjdbc.City;
import com.weather.models.springjdbc.Weather;
import com.weather.models.springjdbc.WeatherType;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class WeatherJdbcRepository {

    private static final RowMapper<Weather> ROW_MAPPER = (rs, rowNum) -> {
        Weather weather = new Weather();
        weather.setId(rs.getInt("id"));
        weather.setTemperature(rs.getDouble("temperature"));
        weather.setDateTime(rs.getObject("date_time", LocalDateTime.class));

        City cityResult = new City();
        cityResult.setId(rs.getInt("city_id"));
        cityResult.setName(rs.getString("name"));
        weather.setCityId(cityResult);

        WeatherType weatherType = new WeatherType();
        weatherType.setId(rs.getInt("weather_type_id"));
        weatherType.setType(rs.getString("type"));
        weather.setWeatherType(weatherType);

        return weather;
    };

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<Weather> findAllByCityId(City city) {
        Map<String, Object> params = Map.of("cityId", city.getId());
        return jdbcTemplate.query("""
                SELECT
                w.*,
                c.name AS name,
                wt.type AS type
                FROM weather w
                JOIN city c ON w.city_id = c.id
                JOIN weather_type wt ON w.weather_type_id = wt.id
                WHERE w.city_id = :cityId
                """, params, ROW_MAPPER);
    }

    public void save(Weather weather) {
        Map<String, Object> params = Map.of(
                "id", weather.getId(),
                "dateTime", weather.getDateTime(),
                "temperature", weather.getTemperature(),
                "cityId", weather.getCityId().getId(),
                "weatherTypeId", weather.getWeatherType().getId());

        jdbcTemplate.update("""
                UPDATE weather SET
                date_time = :dateTime,
                temperature = :temperature,
                city_id = :cityId,
                weather_type_id = :weatherTypeId
                WHERE id = :id
                """, params);
    }
}
