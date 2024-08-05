package com.Optimart.repositories;

import com.Optimart.enums.RoleNameEnum;
import com.Optimart.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
      Role findByName(RoleNameEnum roleName);
}