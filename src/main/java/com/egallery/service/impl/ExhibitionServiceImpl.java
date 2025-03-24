
package com.egallery.service.impl;

import com.egallery.model.entity.Exhibition;
import com.egallery.repository.ExhibitionRepository;
import com.egallery.service.ExhibitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ExhibitionServiceImpl implements ExhibitionService {

    @Autowired
    private ExhibitionRepository exhibitionRepository;

    @Override
    public Exhibition create(Exhibition entity) {
        return exhibitionRepository.save(entity);
    }

    @Override
    public Exhibition getById(UUID id) {
        return exhibitionRepository.findById(id).orElse(null);
    }

    @Override
    public List<Exhibition> getAll() {
        return exhibitionRepository.findAll();
    }

    @Override
    public void delete(UUID id) {
        exhibitionRepository.deleteById(id);
    }
}
