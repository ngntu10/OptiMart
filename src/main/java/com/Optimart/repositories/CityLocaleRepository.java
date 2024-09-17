package com.Optimart.repositories;

import com.Optimart.models.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityLocaleRepository extends JpaRepository<City, Long> {
    Optional<City> findByName(String name);
}
