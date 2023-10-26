package com.weather.repositories.springdatajpa;

import com.weather.models.springdatajpa.WeatherType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherTypeJpaRepository extends JpaRepository<WeatherType, Integer> {
    WeatherType findByType(String type);
}
