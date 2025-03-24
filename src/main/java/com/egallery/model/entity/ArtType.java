
package com.egallery.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtType extends BaseEntity {
    private String name;
    private String slug;
    private String description;
}
