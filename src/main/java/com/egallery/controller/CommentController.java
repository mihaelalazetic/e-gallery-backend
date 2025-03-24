
package com.egallery.controller;

import com.egallery.model.entity.Comment;
import com.egallery.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public Comment create(@RequestBody Comment entity) {
        return commentService.create(entity);
    }

    @GetMapping("/{id}")
    public Comment getById(@PathVariable UUID id) {
        return commentService.getById(id);
    }

    @GetMapping
    public List<Comment> getAll() {
        return commentService.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        commentService.delete(id);
    }
}
