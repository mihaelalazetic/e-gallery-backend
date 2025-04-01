
package com.egallery.service.impl;

import com.egallery.model.dto.ArtworkUploadRequest;
import com.egallery.model.entity.ApplicationUser;
import com.egallery.model.entity.ArtType;
import com.egallery.model.entity.Artwork;
import com.egallery.repository.ArtTypeRepository;
import com.egallery.repository.ArtworkRepository;
import com.egallery.security.SecurityUtils;
import com.egallery.service.ArtworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ArtworkServiceImpl implements ArtworkService {

    @Autowired
    private ArtworkRepository artworkRepository;

    @Autowired
    private ArtTypeRepository artTypeRepository;

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


    public void uploadArtwork(ArtworkUploadRequest request) {
        ApplicationUser artist = SecurityUtils.getCurrentUser();

        ArtType artType = artTypeRepository.findById(request.getArtTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Art type not found"));

        Artwork artwork = new Artwork();
        artwork.setTitle(request.getTitle());
        artwork.setImageUrl(request.getImageUrl());
        artwork.setPrice(request.getPrice());
        artwork.setDimensions(request.getDimensions());
        artwork.setVisibility(request.getVisibility());
        artwork.setSlug(generateSlug(request.getTitle()));
        artwork.setArtist(artist);
        artwork.setArtType(artType);

        artworkRepository.save(artwork);
    }

    private String generateSlug(String title) {
        return title.toLowerCase().replaceAll("\\s+", "-");
    }

    public List<Artwork> findLimited(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("createdAt").descending());
        return artworkRepository.findAll(pageable).getContent();
    }

    public List<Artwork> findPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return artworkRepository.findAll(pageable).getContent();
    }

}
