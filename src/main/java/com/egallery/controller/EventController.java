
package com.egallery.controller;

import com.egallery.model.dto.CreateEventRequest;
import com.egallery.model.entity.Event;
import com.egallery.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/{id}")
    public Event getById(@PathVariable UUID id) {
        return eventService.getById(id);
    }

    @GetMapping
    public List<Event> getAll() {
        return eventService.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        eventService.delete(id);
    }

    @PostMapping("/create")
    public ResponseEntity<Event> create(@ModelAttribute CreateEventRequest request) {
        Event created = eventService.createEventWithExhibition(request);
        return ResponseEntity.ok(created);
    }
}
