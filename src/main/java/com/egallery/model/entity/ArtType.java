package com.egallery.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ✅ ensure auto-gen
    private Long id;

    @CreationTimestamp // ✅ Hibernate will auto-set this on persist
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;
}
