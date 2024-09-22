package com.Optimart.repositories;

import com.Optimart.models.Paymenttype;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentTypeRepository extends JpaRepository<Paymenttype, UUID> {
    Optional<Paymenttype> findByName(String name);
    @NotNull Optional<Paymenttype> findById(@NotNull UUID paymentTypeId);
    Page<Paymenttype> findByNameContainingIgnoreCase(String search, Pageable pageable);
}
