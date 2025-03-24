
package com.egallery.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Artwork extends BaseEntity {
    private String title;
    private String slug;
    private String imageUrl;
    private BigDecimal price;
    private String dimensions;
    private String visibility;

    @ManyToOne
    private ApplicationUser artist;

    @ManyToOne
    private ArtType artType;

    @ManyToMany(mappedBy = "artworks")
    private Set<Exhibition> exhibitions = new HashSet<>();
}
