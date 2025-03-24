
package com.egallery.controller;

import com.egallery.model.entity.Exhibition;
import com.egallery.service.ExhibitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/exhibitions")
public class ExhibitionController {

    @Autowired
    private ExhibitionService exhibitionService;

    @PostMapping
    public Exhibition create(@RequestBody Exhibition entity) {
        return exhibitionService.create(entity);
    }

    @GetMapping("/{id}")
    public Exhibition getById(@PathVariable UUID id) {
        return exhibitionService.getById(id);
    }

    @GetMapping
    public List<Exhibition> getAll() {
        return exhibitionService.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        exhibitionService.delete(id);
    }
}
