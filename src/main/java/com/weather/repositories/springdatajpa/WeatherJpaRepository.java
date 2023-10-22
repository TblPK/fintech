package com.weather.repositories.springdatajpa;

import com.weather.models.springdatajpa.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface WeatherJpaRepository extends JpaRepository<Weather, Integer> {

}
