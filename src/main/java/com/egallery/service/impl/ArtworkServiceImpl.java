
package com.egallery.service.impl;

import com.egallery.model.entity.Artwork;
import com.egallery.repository.ArtworkRepository;
import com.egallery.service.ArtworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ArtworkServiceImpl implements ArtworkService {

    @Autowired
    private ArtworkRepository artworkRepository;

    @Override
    public Artwork create(Artwork entity) {
        return artworkRepository.save(entity);
    }

    @Override
    public Artwork getById(UUID id) {
        return artworkRepository.findById(id).orElse(null);
    }

    @Override
    public List<Artwork> getAll() {
        return artworkRepository.findAll();
    }

    @Override
    public void delete(UUID id) {
        artworkRepository.deleteById(id);
    }
}
