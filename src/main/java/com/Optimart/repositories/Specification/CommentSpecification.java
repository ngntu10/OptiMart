package com.Optimart.repositories.Specification;

import com.Optimart.models.Comment;
import com.Optimart.models.Product;
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

    public static Specification<Comment> searchByKeyword(String search) {
        return (root, query, criteriaBuilder) -> {
            if (search == null || search.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("content")), "%" + search.toLowerCase() + "%")
            );
        };
    }

    public static Specification<Comment> filterCommentByProduct(String product) {
        return Specification.where(byProduct(product));
    }

    public static Specification<Comment> filterCommentByKeyWords(String search){
        return Specification.where(searchByKeyword(search));
    }
}
