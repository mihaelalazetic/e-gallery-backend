
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
@RequestMapping("/api/featured")
public class FeaturedController {

    private final ArtworkService artworkService;

    public FeaturedController(ArtworkService artworkService) {
        this.artworkService = artworkService;
    }

    /** GET /api/featured/artist/{id}/top-artworks?limit=5 */
    @GetMapping("/artist/{artistId}/top-artworks")
    public ResponseEntity<List<ArtworkDto>> topArtworks(
            @PathVariable UUID artistId,
            @RequestParam(defaultValue = "5") int limit
    ) {
        List<ArtworkDto> list =
                artworkService.getTopArtworksForArtist(artistId, limit);
        return ResponseEntity.ok(list);
    }
}
