
package com.egallery.controller;

import com.egallery.model.dto.ApplicationUserDTO;
import com.egallery.model.dto.ArtworkDto;
import com.egallery.model.dto.EventDto;
import com.egallery.model.entity.ApplicationUser;
import com.egallery.model.entity.Artwork;
import com.egallery.model.entity.Category;
import com.egallery.security.SecurityUtils;
import com.egallery.service.ApplicationUserService;
import com.egallery.service.ArtworkService;
import com.egallery.service.CategoryService;
import com.egallery.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/featured")
public class FeaturedController {

    private final ArtworkService artworkService;
    private final CategoryService categoryService;
    private final ApplicationUserService userService;
    private final SecurityUtils securityUtils;
    private final EventService eventService;


    public FeaturedController(ArtworkService artworkService, CategoryService categoryService, ApplicationUserService userService, SecurityUtils securityUtils, EventService eventService) {
        this.artworkService = artworkService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.securityUtils = securityUtils;
        this.eventService = eventService;
    }

    @Operation(
            description = "Get featured artworks for a specific artist",
            summary = "Get featured artworks"
    )
    @GetMapping("/artist/{artistId}/top-artworks")
    public ResponseEntity<List<ArtworkDto>> topArtworks(
            @PathVariable UUID artistId,
            @RequestParam(defaultValue = "5") int limit
    ) {
        List<ArtworkDto> list =
                artworkService.getTopArtworksForArtist(artistId, limit);
        return ResponseEntity.ok(list);
    }


    @GetMapping("/artworks")
    @Operation(
            description = "Get featured artworks",
            summary = "Get featured artworks"
    )
    public Page<ArtworkDto> featured(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        return artworkService.getFeaturedArt(page, size);
    }

    @GetMapping("/new-artworks")
    public ResponseEntity<List<ArtworkDto>> newArtworks(
            @RequestParam(required = false) String filter
    ) {
        List<Artwork> artworks = artworkService.findPaginatedWithFilters(0, 10, null, null, null, null, filter);

        List<ArtworkDto> response = artworks.stream()
                .map(artwork -> {
                    ApplicationUser currentUser = securityUtils.getCurrentUser();
                    return artwork.toDto(currentUser);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/most-liked-artists")
    public ResponseEntity<List<ApplicationUserDTO>> getMostLikedArtists() {
        return ResponseEntity.ok(userService.getMostLikedArtists());
    }
    @GetMapping("/upcoming-events")
    public List<EventDto> getUpcomingEvents() {
        return eventService.getUpcomingEvents()
                .stream()
                .map(EventDto::from)
                .toList();
    }

    @GetMapping("all-artworks")
    public ResponseEntity<List<ArtworkDto>> getArtworks(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String categories,
            @RequestParam(required = false) Integer priceMin,
            @RequestParam(required = false) Integer priceMax,
            @RequestParam(required = false) String filter
    ) {
        List<Artwork> artworks;

        if (size != null && size.equals("*")) {
            // No pagination: return all filtered results
            artworks = artworkService.findAllWithFilters(search, categories, priceMin, priceMax, filter);
        } else {
            // Use default or provided values
            int pageNum = page != null ? page : 0;
            int pageSize = 20;
            artworks = artworkService.findPaginatedWithFilters(pageNum, pageSize, search, categories, priceMin, priceMax, filter);
        }
        List<ArtworkDto> response = artworks.stream()
                .map(artwork -> {
                    ApplicationUser currentUser = securityUtils.getCurrentUser();
                    return artwork.toDto(currentUser);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

}
