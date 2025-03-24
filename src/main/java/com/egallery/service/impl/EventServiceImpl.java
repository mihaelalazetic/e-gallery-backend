
package com.egallery.service.impl;

import com.egallery.model.entity.Event;
import com.egallery.repository.EventRepository;
import com.egallery.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public Event create(Event entity) {
        return eventRepository.save(entity);
    }

    @Override
    public Event getById(UUID id) {
        return eventRepository.findById(id).orElse(null);
    }

    @Override
    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    @Override
    public void delete(UUID id) {
        eventRepository.deleteById(id);
    }
}
