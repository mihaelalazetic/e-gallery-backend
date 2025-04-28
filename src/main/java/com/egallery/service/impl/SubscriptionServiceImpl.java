
package com.egallery.service.impl;

import com.egallery.model.entity.ApplicationUser;
import com.egallery.model.entity.InteractionTargetType;
import com.egallery.model.entity.Subscription;
import com.egallery.repository.SubscriptionRepository;
import com.egallery.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {


    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }
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


    @Override
    public Long toggleSubscription(ApplicationUser subscriber, UUID targetId) {
        var existing = subscriptionRepository.findBySubscriberAndTargetIdAndTargetType(
                subscriber, targetId, InteractionTargetType.USER);
        if (existing.isPresent()) {
            subscriptionRepository.deleteById(existing.get().getId());
        } else {
            var s = new Subscription();
            s.setSubscriber(subscriber);
            s.setTargetId(targetId);
            s.setTargetType(InteractionTargetType.USER);
            subscriptionRepository.save(s);
        }
        return subscriptionRepository.countByTargetIdAndTargetType(targetId, InteractionTargetType.USER);
    }

    @Override
    public boolean isSubscribed(ApplicationUser subscriber, UUID targetId) {
        return subscriptionRepository.findBySubscriberAndTargetIdAndTargetType(
                subscriber, targetId, InteractionTargetType.USER).isPresent();
    }

    @Override
    public Long countSubscribers(UUID targetId) {
        return subscriptionRepository.countByTargetIdAndTargetType(targetId, InteractionTargetType.USER);
    }
}
