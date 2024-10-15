package com.Optimart.repositories;

import com.Optimart.models.Paymenttype;
import com.Optimart.models.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, UUID> {
}
