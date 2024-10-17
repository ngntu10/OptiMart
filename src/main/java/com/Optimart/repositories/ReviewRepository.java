package com.Optimart.repositories;

import com.Optimart.models.Review;
import com.Optimart.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID>, JpaSpecificationExecutor<Review> {
}
