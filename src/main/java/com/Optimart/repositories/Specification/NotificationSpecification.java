package com.Optimart.repositories.Specification;

import com.Optimart.models.Notification;
import com.Optimart.models.Order;
import com.Optimart.models.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class NotificationSpecification {
    public static Specification<Notification> hasUserId(String userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null || userId.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Notification, User> usersJoin = root.join("users");
            return criteriaBuilder.equal(usersJoin.get("id"), UUID.fromString(userId));
        };
    }

    public static Specification<Notification> searchByKeyword(String search) {
        return (root, query, criteriaBuilder) -> {
            if (search == null || search.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + search.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("context")), "%" + search.toLowerCase() + "%")
            );
        };
    }

    public static Specification<Notification> filtersNotification(String userId, String search){
        return Specification.where(hasUserId(userId))
                .and(searchByKeyword(search));
    }
}
