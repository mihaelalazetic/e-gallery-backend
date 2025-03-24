
package com.egallery.service;

import com.egallery.model.entity.Venue;
import java.util.List;
import java.util.UUID;

public interface VenueService {
    Venue create(Venue entity);
    Venue getById(UUID id);
    List<Venue> getAll();
    void delete(UUID id);
}
