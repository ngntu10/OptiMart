package com.Optimart.repositories;

import com.Optimart.models.ProductType;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductTypeRepository extends JpaRepository<ProductType, UUID> {
    Optional<ProductType> findByName(String name);
    @NotNull Optional<ProductType> findById(@NotNull UUID productTypeId);
    Page<ProductType> findByNameContainingIgnoreCase(String search, Pageable pageable);

    Page<ProductType> findAll(Specification<ProductType> productTypeSpecification, Pageable pageable);
}
