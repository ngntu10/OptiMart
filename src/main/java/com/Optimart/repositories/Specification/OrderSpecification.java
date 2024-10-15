package com.Optimart.repositories.Specification;

import com.Optimart.models.Order;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class OrderSpecification {
    public static Specification<Order> byStatus(String status){
        return (root, query, criteriaBuilder) -> {
            if (status == null || status.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            List<String> statusList = Arrays.asList(status.split("-"));
            return root.get("orderStatus").in(statusList);
        };
    }

    public static Specification<Order> hasUserId(UUID userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("user").get("id"), userId);
        };
    }


    public static Specification<Order> filterOrderByUser(String statuses, UUID userId) {
        return (root, query, criteriaBuilder) -> {
            root.fetch("orderItemList", JoinType.LEFT);
            query.distinct(true);
            return Specification.where(byStatus(statuses))
                    .and(hasUserId(userId))
                    .toPredicate(root, query, criteriaBuilder);
        };
    }
}
