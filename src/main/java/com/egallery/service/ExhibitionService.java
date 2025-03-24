
package com.egallery.service;

import com.egallery.model.entity.Exhibition;
import java.util.List;
import java.util.UUID;

public interface ExhibitionService {
    Exhibition create(Exhibition entity);
    Exhibition getById(UUID id);
    List<Exhibition> getAll();
    void delete(UUID id);
}
