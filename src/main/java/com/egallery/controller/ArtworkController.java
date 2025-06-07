
package com.egallery.controller;

import com.egallery.model.dto.ArtworkDto;
import com.egallery.model.dto.ArtworkUploadRequest;
import com.egallery.model.entity.ApplicationUser;
import com.egallery.model.entity.Artwork;
import com.egallery.security.SecurityUtils;
import com.egallery.service.ArtworkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/artworks")
public class ArtworkController {

    private final ArtworkService artworkService;
    private final SecurityUtils securityUtils;

    public ArtworkController(ArtworkService artworkService, SecurityUtils securityUtils) {
        this.artworkService = artworkService;
        this.securityUtils = securityUtils;
    }


    @PostMapping
    public Artwork create(@RequestBody Artwork entity) {
        return artworkService.create(entity);
    }

    @GetMapping("/getById/{id}")
    public ArtworkDto getById(@PathVariable UUID id) {
        return artworkService.getById(id);
    }

    @GetMapping
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


    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        artworkService.delete(id);
    }


    @PostMapping("/upload")
    public ResponseEntity<?> uploadArtwork(@RequestBody ArtworkUploadRequest request) {
        artworkService.uploadArtwork(request);
        return ResponseEntity.ok("Uploaded");
    }

        @GetMapping("/currentUserArtworks")
    public ResponseEntity<List<ArtworkDto>> currentUserArtworks(
    ) {
        List<ArtworkDto> list = artworkService.findByCurrentUser();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/userPublicArtworks/{slug}")
    public ResponseEntity<List<ArtworkDto>> userPublicArtworks(@PathVariable String slug
    ) {
        List<ArtworkDto> list = artworkService.userPublicArtworks(slug);
        return ResponseEntity.ok(list);
    }
}
