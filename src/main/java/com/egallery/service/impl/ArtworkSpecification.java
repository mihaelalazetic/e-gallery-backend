package com.egallery.service.impl;
import com.egallery.model.entity.Artwork;
import com.egallery.model.entity.Category;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ArtworkSpecification {
    public static Specification<Artwork> withFilters(String search, List<String> categories, Integer priceMin, Integer priceMax) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Title or Artist search
            if (search != null && !search.isEmpty()) {
                Predicate titleMatch = criteriaBuilder.like(root.get("title"), "%" + search + "%");
                Predicate artistMatch = criteriaBuilder.like(root.get("artist").get("username"), "%" + search + "%");
                predicates.add(criteriaBuilder.or(titleMatch, artistMatch));
            }

            // Category filter with Join
            if (categories != null && !categories.isEmpty()) {
                Join<Artwork, Category> categoryJoin = root.join("categories");
                predicates.add(categoryJoin.get("id").in(categories));
            }

            // Price range filters
            if (priceMin != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), priceMin));
            }

            if (priceMax != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), priceMax));
            }

            // Apply all predicates
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
