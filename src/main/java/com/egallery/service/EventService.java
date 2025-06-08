
package com.egallery.service;

import com.egallery.model.dto.CreateEventRequest;
import com.egallery.model.entity.ApplicationUser;
import com.egallery.model.entity.Event;
import org.apache.coyote.BadRequestException;

import java.util.List;
import java.util.UUID;

public interface EventService {
    Event getById(UUID id);
    List<Event> getAll();
    void delete(UUID id);
    Event createEventWithExhibition(CreateEventRequest request) throws BadRequestException;
    List<Event> getUpcomingEvents();

    Event getBySlug(String slug);
}
