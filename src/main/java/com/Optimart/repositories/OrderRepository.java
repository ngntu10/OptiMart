package com.Optimart.repositories;

import com.Optimart.models.Order;
import com.Optimart.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    Page<Order> findAll(Specification<Order> specification, Pageable pageable);
    Long countByOrderStatus(int status);
}
