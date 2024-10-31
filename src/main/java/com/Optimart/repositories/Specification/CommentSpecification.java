package com.Optimart.repositories.Specification;

import com.Optimart.models.Comment;
import org.springframework.data.jpa.domain.Specification;
public class CommentSpecification {

    public static Specification<Comment> byProduct(String product) {
        return (root, query, criteriaBuilder) -> {
            if (product == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("product").get("slug"), product);
        };
    }

    public static Specification<Comment> filterCommentByProduct(String product) {
        return Specification.where(byProduct(product));
    }
}
