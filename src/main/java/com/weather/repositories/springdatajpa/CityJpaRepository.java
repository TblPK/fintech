package com.weather.repositories.springdatajpa;

import com.weather.models.springdatajpa.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityJpaRepository extends JpaRepository<City, Integer> {
    City findCityByName(String name);
}
