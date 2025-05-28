package com.egallery.service.impl;

import com.egallery.model.entity.Artwork;
import com.egallery.model.entity.Category;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ArtworkSpecification {
    public static Specification<Artwork> withFilters(String search, String categories, Integer priceMin, Integer priceMax, String filter) {
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
                // Split the comma-separated string into a list of IDs
                List<Long> categoryIds = Stream.of(categories.split(","))
                        .map(Long::valueOf)
                        .toList();

                // Add a predicate for each category ID to ensure the artwork belongs to all specified categories
                for (Long categoryId : categoryIds) {
                    Join<Artwork, Category> categoryJoin = root.join("categories");
                    predicates.add(criteriaBuilder.equal(categoryJoin.get("id"), categoryId));
                }
            }

            // Price range filters
            if (priceMin != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), priceMin));
            }

            if (priceMax != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), priceMax));
            }
            LocalDateTime now = LocalDateTime.now();
            // Date filters
            if ("thisWeek".equals(filter)) {
                LocalDateTime startOfWeek = now.with(java.time.DayOfWeek.MONDAY).toLocalDate().atStartOfDay();
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startOfWeek));
            }

            if ("thisMonth".equals(filter)) {
                LocalDateTime startOfMonth = now.with(TemporalAdjusters.firstDayOfMonth()).toLocalDate().atStartOfDay();
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startOfMonth));
            }

            // Apply all predicates
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
