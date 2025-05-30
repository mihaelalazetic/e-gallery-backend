package com.egallery.service.impl;

import com.egallery.model.dto.ArtworkDto;
import com.egallery.model.dto.ArtworkUploadRequest;
import com.egallery.model.entity.ApplicationUser;
import com.egallery.model.entity.Artwork;
import com.egallery.model.entity.ArtworkImage;
import com.egallery.model.entity.Category;
import com.egallery.repository.ArtworkRepository;
import com.egallery.repository.CategoryRepository;
import com.egallery.security.SecurityUtils;
import com.egallery.service.ArtworkService;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArtworkServiceImpl implements ArtworkService {

    private final ArtworkRepository artworkRepository;
    private final CategoryRepository categoryRepository;
    private final SecurityUtils securityUtils;

    public ArtworkServiceImpl(ArtworkRepository artworkRepository,
                              CategoryRepository categoryRepository, SecurityUtils securityUtils) {
        this.artworkRepository = artworkRepository;
        this.categoryRepository = categoryRepository;
        this.securityUtils = securityUtils;
    }

    @Override
    public Artwork create(Artwork entity) {
        return artworkRepository.save(entity);
    }

    @Override
    public ArtworkDto getById(UUID id) {
        return Objects.requireNonNull(artworkRepository.findById(id).orElse(null)).toDto(securityUtils.getCurrentUser());
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

        List<Category> categories = categoryRepository.findAllById(request.getCategoryIds());
        if (categories.isEmpty()) {
            throw new IllegalArgumentException("Art type not found");
        }

        Artwork artwork = new Artwork();
        artwork.setTitle(request.getTitle());
        artwork.setPrice(request.getPrice());
        artwork.setDimensions(request.getDimensions());
        artwork.setVisibility(request.getVisibility());
        artwork.setSlug(generateSlug(request.getTitle()));
        artwork.setDescription(request.getDescription());
        artwork.setArtist(artist);
        artwork.setCategories(new HashSet<>(categories));

        // Add multiple images to the artwork
        Set<ArtworkImage> artworkImages = new HashSet<>();
        int position = 0;
        for (String imageUrl : request.getImageUrl()) {
            ArtworkImage image = new ArtworkImage();
            image.setImageUrl(imageUrl);
            image.setPosition(position++);
            artworkImages.add(image);
        }
        artwork.setImages(artworkImages);

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
    public Page<ArtworkDto> getFeaturedArt(int page, int size)  {
        Page<Artwork> pageEntity =
                artworkRepository.findAllOrderByLikesDesc(PageRequest.of(page, size));
        ApplicationUser current = securityUtils.getCurrentUser();

        List<ArtworkDto> dtos = pageEntity.getContent().stream().map(a -> {
            ArtworkDto dto = new ArtworkDto();
            dto.setId(a.getId());
            dto.setTitle(a.getTitle());
            dto.setImageUrls(a.getImages().stream()
                    .sorted(Comparator.comparing(ArtworkImage::getPosition))
                    .map(ArtworkImage::getImageUrl)
                    .collect(Collectors.toList()));
            dto.setDescription(a.getDescription());
            dto.setPrice(a.getPrice());
            dto.setArtist(a.getArtist().mapToDto());
            dto.setLikes((long) a.getLikes().size());
            dto.setCommentCount((long) a.getComments().size());

            boolean isLiked = current != null && a.getLikes().stream()
                    .anyMatch(pl -> pl.getApplicationUser().getId().equals(current.getId()));
            dto.setLiked(isLiked);

            return dto;
        }).toList();

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
    public Long countLikedArtworks(UUID userId) {
        return artworkRepository.countTotalLikesByArtist(userId);
    }

    public List<Artwork> findByArtistId(UUID userId) {
        return artworkRepository.findByArtistId(userId);
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
                    dto.setCommentCount((long) a.getComments().size());
                    dto.setLiked(
                            current != null &&
                                    a.getLikes().stream()
                                            .anyMatch(pl -> pl.getApplicationUser().getId().equals(current.getId()))
                    );
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<Artwork> findPaginatedWithFilters(int page, int size, String search, String categories, Integer priceMin, Integer priceMax, String filter) {
        PageRequest pageable = PageRequest.of(page, size);

        // Use the withFilters method from ArtworkSpecification
        Specification<Artwork> spec = ArtworkSpecification.withFilters(search, categories, priceMin, priceMax, filter);

        return artworkRepository.findAll(spec, pageable).getContent();
    }

    public List<ArtworkDto> findByCurrentUser() {
        ApplicationUser currentUser = securityUtils.getCurrentUser();
        if (currentUser == null) {
            return Collections.emptyList();
        }

        return findByArtistId(currentUser.getId()).stream()
                .sorted(Comparator.comparing(Artwork::getCreatedAt).reversed())
                .map(artwork -> artwork.toDto(currentUser))
                .toList();
    }

}

