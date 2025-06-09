package com.egallery.service.impl;

import com.egallery.model.dto.ApplicationUserDTO;
import com.egallery.model.dto.ArtworkDto;
import com.egallery.model.dto.ArtworkUploadRequest;
import com.egallery.model.entity.*;
import com.egallery.repository.ArtworkRepository;
import com.egallery.repository.CategoryRepository;
import com.egallery.security.SecurityUtils;
import com.egallery.service.ApplicationUserService;
import com.egallery.service.ArtworkLinkService;
import com.egallery.service.ArtworkService;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArtworkServiceImpl implements ArtworkService {

    private final ArtworkRepository artworkRepository;
    private final CategoryRepository categoryRepository;
    private final SecurityUtils securityUtils;
    private final ApplicationUserService applicationUserService;
    private final ArtworkLinkService artworkLinkService;

    public ArtworkServiceImpl(ArtworkRepository artworkRepository,
                              CategoryRepository categoryRepository, SecurityUtils securityUtils, @Lazy ApplicationUserService applicationUserService, ArtworkLinkService artworkLinkService) {
        this.artworkRepository = artworkRepository;
        this.categoryRepository = categoryRepository;
        this.securityUtils = securityUtils;
        this.applicationUserService = applicationUserService;
        this.artworkLinkService = artworkLinkService;
    }

    @Override
    public Artwork create(Artwork entity) {
        return artworkRepository.save(entity);
    }

    @Override
    public ArtworkDto getById(UUID id) {
        Artwork artwork = Objects.requireNonNull(artworkRepository.findById(id).orElse(null));
        ApplicationUser currentUser = securityUtils.getCurrentUser();

        ArtworkDto dto = artwork.toDto(currentUser);

        List<ArtworkLink> links = artworkLinkService.findByArtworkId(artwork.getId());
        dto.setLinks(links);

        return dto;
    }

    @Override
    public List<Artwork> getAll() {
        return artworkRepository.findAll();
    }

    @Override
    public void delete(UUID id) {
        artworkRepository.deleteById(id);
    }

    @SneakyThrows
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

        Artwork artwork1 = artworkRepository.save(artwork);

        Map<String, String> socialMediaLabels = Map.of(
                "instagram", "Instagram",
                "facebook", "Facebook",
                "twitter", "Twitter",
                "linkedin", "LinkedIn",
                "youtube", "YouTube",
                "tiktok", "TikTok",
                "pinterest", "Pinterest",
                "etsy", "Etsy"
        );

        for (String link : request.getRelevantLinks()) {
            String label = "Other";
            // Default label for non-popular links
            for (Map.Entry<String, String> entry : socialMediaLabels.entrySet()) {
                if (link.contains(entry.getKey())) {
                    label = entry.getValue();
                    break;
                }
            }
            if (label.trim().isEmpty() || label.equals("Other")) {
                URI uri = new URI(link);
                String host = uri.getHost();
                if (host != null) {
                    String[] parts = host.split("\\.");
                    if (parts.length > 1) {
                        label = parts[parts.length - 2].substring(0, 1).toUpperCase() + parts[parts.length - 2].substring(1);
                    }
                }
            }

            ArtworkLink artworkLink = new ArtworkLink(label, link, artwork1);
            artworkLinkService.create(artworkLink);
        }
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
        if ("thisMonth".equals(filter)) {
            size = 8;
        }
        PageRequest pageable = PageRequest.of(page, size);

        // Use the withFilters method from ArtworkSpecification
        Specification<Artwork> spec = ArtworkSpecification.withFilters(search, categories, priceMin, priceMax, filter);

        return artworkRepository.findAll(spec, pageable).getContent();
    }

    public List<Artwork> findAllWithFilters(String search, String categories, Integer priceMin, Integer priceMax, String filter) {
        Specification<Artwork> spec = ArtworkSpecification.withFilters(search, categories, priceMin, priceMax, filter);
        return artworkRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "createdAt"));
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

    public List<ArtworkDto> userPublicArtworks(String slug) {
        ApplicationUserDTO userDTO = (ApplicationUserDTO) applicationUserService.getBySlug(slug);
        ApplicationUser user = applicationUserService.getById(userDTO.getId());
        if (user == null) {
            return Collections.emptyList();
        }

        return findByArtistId(user.getId()).stream()
                .sorted(Comparator.comparing(Artwork::getCreatedAt).reversed())
                .filter(artwork -> Objects.equals(artwork.getVisibility(), "public"))
                .map(artwork -> artwork.toDto(user))
                .toList();
    }
}

