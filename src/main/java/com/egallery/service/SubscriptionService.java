
package com.egallery.service;

import com.egallery.model.entity.Subscription;
import java.util.List;
import java.util.UUID;

public interface SubscriptionService {
    Subscription create(Subscription entity);
    Subscription getById(UUID id);
    List<Subscription> getAll();
    void delete(UUID id);
}
