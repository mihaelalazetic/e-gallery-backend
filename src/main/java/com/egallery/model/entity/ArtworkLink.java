
package com.egallery.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtworkLink extends BaseEntity {
    private String label;
    private String url;

    @ManyToOne
    private Artwork artwork;
}
