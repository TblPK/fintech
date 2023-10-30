package com.weather.repositories.springdatajpa;

import com.weather.models.springdatajpa.City;
import com.weather.models.springdatajpa.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeatherJpaRepository extends JpaRepository<Weather, Integer> {
    List<Weather> findAllByCityId(City city);
}
