
package com.egallery.controller;

import com.egallery.model.entity.ArtType;
import com.egallery.service.ArtTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/arttypes")
public class ArtTypeController {

    @Autowired
    private ArtTypeService artTypeService;

    @PostMapping
    public ArtType create(@RequestBody ArtType entity) {
        return artTypeService.create(entity);
    }

    @GetMapping("/{id}")
    public ArtType getById(@PathVariable Long id) {
        return artTypeService.getById(id);
    }

    @GetMapping
    public List<ArtType> getAll() {
        return artTypeService.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        artTypeService.delete(id);
    }
}
