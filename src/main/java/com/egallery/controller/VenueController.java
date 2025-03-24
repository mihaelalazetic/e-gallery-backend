
package com.egallery.controller;

import com.egallery.model.entity.Venue;
import com.egallery.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/venues")
public class VenueController {

    @Autowired
    private VenueService venueService;

    @PostMapping
    public Venue create(@RequestBody Venue entity) {
        return venueService.create(entity);
    }

    @GetMapping("/{id}")
    public Venue getById(@PathVariable UUID id) {
        return venueService.getById(id);
    }

    @GetMapping
    public List<Venue> getAll() {
        return venueService.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        venueService.delete(id);
    }
}
