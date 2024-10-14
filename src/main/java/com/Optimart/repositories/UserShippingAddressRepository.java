package com.Optimart.repositories;

import com.Optimart.models.userShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserShippingAddressRepository extends JpaRepository<userShippingAddress, UUID> {
}
