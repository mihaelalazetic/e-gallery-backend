
package com.egallery.service.impl;

import com.egallery.model.entity.Venue;
import com.egallery.repository.VenueRepository;
import com.egallery.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VenueServiceImpl implements VenueService {

    @Autowired
    private VenueRepository venueRepository;

    @Override
    public Venue create(Venue entity) {
        return venueRepository.save(entity);
    }

    @Override
    public Venue getById(UUID id) {
        return venueRepository.findById(id).orElse(null);
    }

    @Override
    public List<Venue> getAll() {
        return venueRepository.findAll();
    }

    @Override
    public void delete(UUID id) {
        venueRepository.deleteById(id);
    }
}
