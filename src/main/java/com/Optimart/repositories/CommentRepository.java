package com.Optimart.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<CommentRepository, UUID> {
}
