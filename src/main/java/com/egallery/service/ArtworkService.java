
package com.egallery.service;

import com.egallery.model.entity.Artwork;
import java.util.List;
import java.util.UUID;

public interface ArtworkService {
    Artwork create(Artwork entity);
    Artwork getById(UUID id);
    List<Artwork> getAll();
    void delete(UUID id);
}
