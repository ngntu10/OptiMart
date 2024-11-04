package com.Optimart.repositories.Specification;

import com.Optimart.models.City;
import com.Optimart.models.Order;
import com.Optimart.models.ShippingAddress;
import com.Optimart.models.User;
import jakarta.persistence.criteria.Join;
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
            List<String> statusList = Arrays.asList(status.split("\\|"));
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

    public static Specification<Order> hasCityId(String cityId) {
        return (root, query, criteriaBuilder) -> {
            if (cityId == null || cityId.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            List<String> cityIdListArray = Arrays.asList(cityId.split("\\|"));
            Join<Order, ShippingAddress> shippingAddressJoin = root.join("shippingAddress");
            Join<ShippingAddress, City> cityJoin = shippingAddressJoin.join("city");
            return cityJoin.get("id").in(cityIdListArray);
        };
    }


    public static Specification<Order> filterOrderByUser(String statuses, UUID userId, String cityId, boolean fetchOrderItems) {
        if (statuses == null && userId == null && cityId == null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
        return (root, query, criteriaBuilder) -> {
            if (fetchOrderItems) {
                root.fetch("orderItemList", JoinType.LEFT);
                query.distinct(true);
            }
            query.distinct(true);
            return Specification.where(byStatus(statuses))
                    .and(hasUserId(userId))
                    .and(hasCityId(cityId))
                    .toPredicate(root, query, criteriaBuilder);
        };
    }


    public static Specification<Order> searchByKeyword(String search) {
        return (root, query, criteriaBuilder) -> {
            if (search == null || search.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            Join<Order, ShippingAddress> shippingAddressJoin = root.join("shippingAddress");
            return criteriaBuilder.or(
                    criteriaBuilder.like(
                            criteriaBuilder.lower(shippingAddressJoin.get("fullName")),
                            "%" + search.toLowerCase() + "%"
                    )
            );
        };
    }


    public static Specification<Order> filterOrder(String statuses, String cityId, String search) {
        if (statuses == null && cityId == null && search == null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return Specification.where(byStatus(statuses))
                    .and(hasCityId(cityId))
                    .and(searchByKeyword(search))
                    .toPredicate(root, query, criteriaBuilder);
        };
    }
}
