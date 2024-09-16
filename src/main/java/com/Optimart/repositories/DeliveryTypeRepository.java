package com.Optimart.repositories;

import com.Optimart.models.DeliveryType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryTypeRepository extends JpaRepository<DeliveryType, UUID> {
    Optional<DeliveryType> findByName(String name);
}
