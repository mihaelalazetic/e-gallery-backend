
package com.egallery.service;

import com.egallery.model.entity.ArtworkLink;
import java.util.List;
import java.util.UUID;

public interface ArtworkLinkService {
    ArtworkLink create(ArtworkLink entity);
    ArtworkLink getById(UUID id);
    List<ArtworkLink> getAll();
    void delete(UUID id);
}
