
package com.egallery.service.impl;

import com.egallery.model.entity.ArtworkLink;
import com.egallery.repository.ArtworkLinkRepository;
import com.egallery.service.ArtworkLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ArtworkLinkServiceImpl implements ArtworkLinkService {

    @Autowired
    private ArtworkLinkRepository artworkLinkRepository;

    @Override
    public ArtworkLink create(ArtworkLink entity) {
        return artworkLinkRepository.save(entity);
    }

    @Override
    public ArtworkLink getById(UUID id) {
        return artworkLinkRepository.findById(id).orElse(null);
    }

    @Override
    public List<ArtworkLink> getAll() {
        return artworkLinkRepository.findAll();
    }

    @Override
    public void delete(UUID id) {
        artworkLinkRepository.deleteById(id);
    }

    @Override
    public List<ArtworkLink> findByArtworkId(UUID artworkId) {
        return artworkLinkRepository.findByArtworkId(artworkId);
    }
}
