
package com.egallery.repository;

import com.egallery.model.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
}
