package com.Optimart.repositories;

import com.Optimart.dto.User.UserSearchDTO;
import com.Optimart.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserRepository  extends JpaRepository<User, UUID> , JpaSpecificationExecutor<User> {
    @Query("SELECT u FROM User u WHERE " +
            "(:roleIds IS NULL OR u.role.id IN :roleIds) AND " + "(:statuses IS NULL OR u.status IN :statuses) AND " +
            "(:cityIds IS NULL OR u.city.id IN :cityIds) AND " + "(:userTypes IS NULL OR u.userType IN :userTypes) AND " +
            "(:search IS NULL OR u.fullName LIKE %:search% OR u.email LIKE %:search%)")
    Page<User> findUsersWithFillter(@Param("roleIds") List<Integer> roleIds, @Param("statuses") List<Integer> statuses,
                                    @Param("cityIds") List<Integer> cityIds, @Param("userTypes") List<Integer> userTypes,
                                    @Param("search") String search, Pageable pageable);

    Page<User> findUsersWithSpec(Specification<User> spec);
}
