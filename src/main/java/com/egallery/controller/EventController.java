
package com.egallery.controller;

import com.egallery.model.dto.CreateEventRequest;
import com.egallery.model.dto.EventDto;
import com.egallery.model.entity.Event;
import com.egallery.security.SecurityUtils;
import com.egallery.service.EventService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/create")
    public ResponseEntity<Event> create(@RequestBody CreateEventRequest request) throws BadRequestException {
        Event created = eventService.createEventWithExhibition(request);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public Event getById(@PathVariable UUID id) {
        return eventService.getById(id);
    }
    @GetMapping("/slug/{slug}")
    public Event getBySlug(@PathVariable String slug) {
        return eventService.getBySlug(slug);
    }

    @GetMapping("/all")
    public List<EventDto> getAll() {
        return eventService.getAll() .stream()
                .map(EventDto::from)
                .toList();
    }

    @GetMapping("/upcoming")
    public List<EventDto> getUpcomingEvents() {
        return eventService.getUpcomingEvents()
                .stream()
                .map(EventDto::from)
                .toList();
    }
    @GetMapping("/my-events")
    public List<EventDto> getMyEvents() {
        return eventService.getMyEvents()
                .stream()
                .map(EventDto::from)
                .toList();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        eventService.delete(id);
    }


}
