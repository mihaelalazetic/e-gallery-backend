
package com.egallery.controller;

import com.egallery.model.entity.Artwork;
import com.egallery.service.ArtworkService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Artwork> getAll() {
        return artworkService.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        artworkService.delete(id);
    }
}
