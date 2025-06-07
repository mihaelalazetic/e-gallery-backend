
package com.egallery.model.entity;

import com.egallery.model.enums.InteractionTargetType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"subscriber_id", "targetId", "targetType"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subscription extends BaseEntity {

    @ManyToOne
    private ApplicationUser subscriber;

    @Column(nullable = false)
    private UUID targetId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InteractionTargetType targetType;

    private Boolean notificationEnabled = true;
}
