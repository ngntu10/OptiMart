package com.Optimart.repositories.Specification;

import com.Optimart.models.Review;
import com.Optimart.models.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class ReviewSpecification {
    public static Specification<Review> searchByKeyword(String search) {
        return (root, query, criteriaBuilder) -> {
            if (search == null || search.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Review, User> userJoin = root.join("user", JoinType.INNER);
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("content")), "%" + search.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(userJoin.get("fullName")), "%" + search.toLowerCase() + "%")
            );
        };
    }
    public static Specification<Review> filterReviews(String search) {
        return Specification.where(searchByKeyword(search));
    }
}
