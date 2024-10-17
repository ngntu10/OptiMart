package com.Optimart.repositories.Specification;

import com.Optimart.models.Product;
import com.Optimart.models.Review;
import com.Optimart.models.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

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

    public static Specification<Review> hasProductId(String productId){
        return (root, query, criteriaBuilder) -> {
            if (productId == null || productId.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            UUID productUUID = UUID.fromString(productId);
            Join<Review, Product> productJoin = root.join("product", JoinType.INNER);
            return criteriaBuilder.equal(productJoin.get("id"), productUUID);
        };
    }

    public static Specification<Review> filterReviews(String search) {
        return Specification.where(searchByKeyword(search));
    }

    public static Specification<Review> filterReviewsByProductId(String productId) {
        return Specification.where(hasProductId(productId));
    }
}
