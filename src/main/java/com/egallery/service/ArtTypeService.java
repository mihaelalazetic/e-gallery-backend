
package com.egallery.service;

import com.egallery.model.entity.ArtType;
import java.util.List;
import java.util.UUID;

public interface ArtTypeService {
    ArtType create(ArtType entity);
    ArtType getById(Long id);
    List<ArtType> getAll();
    void delete(Long id);

}
