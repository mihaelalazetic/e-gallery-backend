package com.egallery.service.impl;

import com.egallery.model.dto.ArtworkDto;
import com.egallery.model.dto.ArtworkUploadRequest;
import com.egallery.model.entity.ApplicationUser;
import com.egallery.model.entity.ArtType;
import com.egallery.model.entity.Artwork;
import com.egallery.repository.ArtTypeRepository;
import com.egallery.repository.ArtworkRepository;
import com.egallery.security.SecurityUtils;
import com.egallery.service.ArtworkService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ArtworkServiceImpl implements ArtworkService {

    private final ArtworkRepository artworkRepository;
    private final ArtTypeRepository artTypeRepository;
    private final SecurityUtils securityUtils;

    public ArtworkServiceImpl(ArtworkRepository artworkRepository,
                              ArtTypeRepository artTypeRepository, SecurityUtils securityUtils) {
        this.artworkRepository = artworkRepository;
        this.artTypeRepository = artTypeRepository;
        this.securityUtils = securityUtils;
    }

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

    @Override
    public void uploadArtwork(ArtworkUploadRequest request) {
        ApplicationUser artist = securityUtils.getCurrentUser();

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

    @Override
    public List<Artwork> findLimited(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("createdAt").descending());
        return artworkRepository.findAll(pageable).getContent();
    }

    @Override
    public Page<ArtworkDto> getFeaturedArt(int page, int size) {
        Page<Artwork> pageEntity =
                artworkRepository.findAllOrderByLikesDesc(PageRequest.of(page, size));
        ApplicationUser current = securityUtils.getCurrentUser();

        List<ArtworkDto> dtos = new ArrayList<>(
                pageEntity.getContent().stream().map(a -> {
                    ArtworkDto dto = new ArtworkDto();
                    dto.setId(a.getId());
                    dto.setTitle(a.getTitle());
                    dto.setImageUrl(a.getImageUrl());
                    dto.setDescription(a.getDescription());
                    dto.setPrice(a.getPrice());
                    dto.setArtist(a.getArtist().mapToDto());
                    dto.setLikes((long) a.getLikes().size());
                    dto.setComments((long) a.getComments().size());
                    boolean isLiked = current != null && a.getLikes().stream()
                            .anyMatch(pl -> pl.getApplicationUser().getId().equals(current.getId()));
                    dto.setLiked(isLiked);
                    return dto;
                }).toList()
        );

        Collections.shuffle(dtos);

        return new PageImpl<>(
                dtos,
                pageEntity.getPageable(),
                pageEntity.getTotalElements()
        );
    }

    @Override
    public List<Artwork> findPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return artworkRepository.findAll(pageable).getContent();
    }

    public Long countByUserId(UUID userId) {
        return artworkRepository.countByArtistId(userId);
    }

    @Override
    public List<ArtworkDto> getTopArtworksForArtist(UUID artistId, int limit) {
        Pageable pageReq = PageRequest.of(0, limit);
        List<Artwork> top = artworkRepository
                .findTopByArtistOrderByLikesDesc(artistId, pageReq);

        ApplicationUser current = securityUtils.getCurrentUser();
        return top.stream()
                .map(a -> {
                    ArtworkDto dto = a.toDto(current);
                    dto.setLikes((long) a.getLikes().size());
                    dto.setComments((long) a.getComments().size());
                    dto.setLiked(
                            current != null &&
                                    a.getLikes().stream()
                                            .anyMatch(pl -> pl.getApplicationUser().getId().equals(current.getId()))
                    );
                    return dto;
                })
                .collect(Collectors.toList());
    }
}

