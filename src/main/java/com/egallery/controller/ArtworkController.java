
package com.egallery.controller;

import com.egallery.model.dto.ArtworkDto;
import com.egallery.model.dto.ArtworkUploadRequest;
import com.egallery.model.entity.ApplicationUser;
import com.egallery.model.entity.Artwork;
import com.egallery.security.SecurityUtils;
import com.egallery.service.ArtworkService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    @GetMapping("/{id}")
    public Artwork getById(@PathVariable UUID id) {
        return artworkService.getById(id);
    }

    @GetMapping
    public ResponseEntity<List<ArtworkDto>> getArtworks(
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            Authentication authentication
    ) {
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated();

        if (!isAuthenticated && limit == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Artwork> artworks = (limit != null)
                ? artworkService.findLimited(limit)
                : artworkService.findPaginated(page, size);

        // fetch the current user once
        ApplicationUser currentUser = securityUtils.getCurrentUser();

        // map each Artwork → ArtworkDto with liked state
        List<ArtworkDto> response = artworks.stream()
                .map(a -> a.toDto(currentUser))
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


    @GetMapping("/featured")
    public Page<ArtworkDto> featured(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        return artworkService.getFeaturedArt(page, size);
    }


}
