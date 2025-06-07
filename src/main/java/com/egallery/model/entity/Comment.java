
package com.egallery.model.entity;

import com.egallery.model.enums.InteractionTargetType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseEntity {

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @ManyToOne
    private ApplicationUser user;

    @Enumerated(EnumType.STRING)
    private InteractionTargetType targetType;

    @Column(nullable = false)
    private UUID targetId;

    @ManyToOne
    private Comment parent;

    private Boolean isVisible = true;
}
