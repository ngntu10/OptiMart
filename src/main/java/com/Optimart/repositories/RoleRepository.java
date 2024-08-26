package com.Optimart.repositories;

import com.Optimart.models.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Page<Role> findByNameContainingIgnoreCase(String search, Pageable pageable);
    Optional<Role> findByName(String name);
}