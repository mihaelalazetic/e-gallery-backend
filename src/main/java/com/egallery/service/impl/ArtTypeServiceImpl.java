
package com.egallery.service.impl;

import com.egallery.model.entity.ArtType;
import com.egallery.repository.ArtTypeRepository;
import com.egallery.service.ArtTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ArtTypeServiceImpl implements ArtTypeService {

    @Autowired
    private ArtTypeRepository artTypeRepository;

    @Override
    public ArtType create(ArtType entity) {
        return artTypeRepository.save(entity);
    }

    @Override
    public ArtType getById(Long id) {
        return artTypeRepository.getReferenceById(id);
    }


    @Override
    public List<ArtType> getAll() {
        return artTypeRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        artTypeRepository.deleteById(id);
    }
}
