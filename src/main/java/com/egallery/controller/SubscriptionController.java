
package com.egallery.controller;

import com.egallery.model.entity.ApplicationUser;
import com.egallery.model.entity.Subscription;
import com.egallery.security.SecurityUtils;
import com.egallery.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final SecurityUtils securityUtils;

    public SubscriptionController(SubscriptionService subscriptionService, SecurityUtils securityUtils) {
        this.subscriptionService = subscriptionService;
        this.securityUtils = securityUtils;
    }
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


    @PostMapping("/toggle/{userId}")
    public ResponseEntity<Long> toggleFollow(@PathVariable UUID userId) {
        ApplicationUser me = securityUtils.getCurrentUser();
        Long newCount = subscriptionService.toggleSubscription(me, userId);
        return ResponseEntity.ok(newCount);
    }

    @GetMapping("/count/{userId}")
    public ResponseEntity<Long> getFollowerCount(@PathVariable UUID userId) {
        return ResponseEntity.ok(subscriptionService.countSubscribers(userId));
    }

    @GetMapping("/is-following/{userId}")
    public ResponseEntity<Boolean> isFollowing(@PathVariable UUID userId) {
        ApplicationUser me = securityUtils.getCurrentUser();
        return ResponseEntity.ok(subscriptionService.isSubscribed(me, userId));
    }
}
