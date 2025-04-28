
package com.egallery.repository;

import com.egallery.model.entity.ApplicationUser;
import com.egallery.model.entity.InteractionTargetType;
import com.egallery.model.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {    Long countByTargetIdAndTargetType(UUID targetId, InteractionTargetType targetType);
    Optional<Subscription> findBySubscriberAndTargetIdAndTargetType(
            ApplicationUser subscriber,
            UUID targetId,
            InteractionTargetType targetType);

}
