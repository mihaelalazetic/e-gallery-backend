
package com.egallery.controller;

import com.egallery.model.entity.ArtworkLink;
import com.egallery.service.ArtworkLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/artworklinks")
public class ArtworkLinkController {

    @Autowired
    private ArtworkLinkService artworkLinkService;

    @PostMapping
    public ArtworkLink create(@RequestBody ArtworkLink entity) {
        return artworkLinkService.create(entity);
    }

    @GetMapping("/{id}")
    public ArtworkLink getById(@PathVariable UUID id) {
        return artworkLinkService.getById(id);
    }

    @GetMapping
    public List<ArtworkLink> getAll() {
        return artworkLinkService.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        artworkLinkService.delete(id);
    }
}
