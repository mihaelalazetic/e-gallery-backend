
package com.egallery.controller;

import com.egallery.model.entity.PostLike;
import com.egallery.service.PostLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/likes")
public class PostLikeController {

    @Autowired
    private PostLikeService likeService;

    @PostMapping
    public PostLike create(@RequestBody PostLike entity) {
        return likeService.create(entity);
    }

    @GetMapping("/{id}")
    public PostLike getById(@PathVariable UUID id) {
        return likeService.getById(id);
    }

    @GetMapping
    public List<PostLike> getAll() {
        return likeService.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        likeService.delete(id);
    }
}
