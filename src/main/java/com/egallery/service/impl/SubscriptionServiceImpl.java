
package com.egallery.service.impl;

import com.egallery.model.entity.Subscription;
import com.egallery.repository.SubscriptionRepository;
import com.egallery.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Override
    public Subscription create(Subscription entity) {
        return subscriptionRepository.save(entity);
    }

    @Override
    public Subscription getById(UUID id) {
        return subscriptionRepository.findById(id).orElse(null);
    }

    @Override
    public List<Subscription> getAll() {
        return subscriptionRepository.findAll();
    }

    @Override
    public void delete(UUID id) {
        subscriptionRepository.deleteById(id);
    }
}
