package com.Optimart.repositories.Specification;

import com.Optimart.models.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserSpecification {
    public static Specification<User> byRoleIds(String roleIds){
        return (root, query, criteriaBuilder) -> {
            if (roleIds == null || roleIds.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            List<UUID> roleIdList = Arrays.stream(roleIds.split(" "))
                    .map(UUID::fromString)
                    .collect(Collectors.toList());
            System.out.println(roleIdList); 
            return root.get("role").get("id").in(roleIdList);
        };
    }
    public static Specification<User> hasStatuses(String status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null || status.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            List<String> statusList = Arrays.asList(status.split("-"));
            return root.get("status").in(statusList);
        };
    }
    public static Specification<User> hasUserTypes(String userTypes) {
        return (root, query, criteriaBuilder) -> {
            if (userTypes == null || userTypes.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            List<String> userTypeArray = Arrays.asList(userTypes.split("-"));
            return root.get("userType").in(userTypeArray);
        };
    }
    public static Specification<User> hasCityId(String cityId) {
        return (root, query, criteriaBuilder) -> {
            if (cityId == null || cityId.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            List<String> cityIdListArray = Arrays.asList(cityId.split("-"));
            return root.get("city").get("id").in(cityIdListArray);
        };
    }

    public static Specification<User> searchByKeyword(String search) {
        return (root, query, criteriaBuilder) -> {
            if (search == null || search.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), "%" + search.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + search.toLowerCase() + "%")
            );
        };
    }
    public static Specification<User> filterUsers(String roleIds, String statuses,String cityIds, String userTypes, String keyword) {
        return Specification.where(byRoleIds(roleIds))
                .and(hasStatuses(statuses))
                .and(hasUserTypes(userTypes))
                .and(hasCityId(cityIds))
                .and(searchByKeyword(keyword));
    }
}
