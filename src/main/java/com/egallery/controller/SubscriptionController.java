
package com.egallery.controller;

import com.egallery.model.entity.Subscription;
import com.egallery.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping
    public Subscription create(@RequestBody Subscription entity) {
        return subscriptionService.create(entity);
    }

    @GetMapping("/{id}")
    public Subscription getById(@PathVariable UUID id) {
        return subscriptionService.getById(id);
    }

    @GetMapping
    public List<Subscription> getAll() {
        return subscriptionService.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        subscriptionService.delete(id);
    }
}
