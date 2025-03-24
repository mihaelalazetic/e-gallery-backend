
package com.egallery.service;

import com.egallery.model.entity.Event;
import java.util.List;
import java.util.UUID;

public interface EventService {
    Event create(Event entity);
    Event getById(UUID id);
    List<Event> getAll();
    void delete(UUID id);
}
