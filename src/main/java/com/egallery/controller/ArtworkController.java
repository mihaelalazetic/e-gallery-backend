
package com.egallery.controller;

import com.egallery.model.dto.ArtworkDto;
import com.egallery.model.dto.ArtworkUploadRequest;
import com.egallery.model.entity.Artwork;
import com.egallery.service.ArtworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/artworks")
public class ArtworkController {

    @Autowired
    private ArtworkService artworkService;

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
            Authentication authentication // Spring injects this
    ) {
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated();

        // If user is NOT logged in and limit is not set → block it
        if (!isAuthenticated && limit == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Artwork> artworks;

        if (limit != null) {
            artworks = artworkService.findLimited(limit);
        } else {
            artworks = artworkService.findPaginated(page, size);
        }

        List<ArtworkDto> response = artworks.stream()
                .map(ArtworkDto::new)
                .toList();

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
    public Page<Artwork> featured(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        return artworkService.getFeaturedArt(page, size);
    }


}
