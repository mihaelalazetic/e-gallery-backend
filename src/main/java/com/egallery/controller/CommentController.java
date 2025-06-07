// src/main/java/com/egallery/controller/CommentController.java

package com.egallery.controller;

import com.egallery.model.entity.Comment;
import com.egallery.model.entity.ApplicationUser;
import com.egallery.model.enums.InteractionTargetType;
import com.egallery.security.SecurityUtils;
import com.egallery.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final SecurityUtils securityUtils;

    public CommentController(
            CommentService commentService,
            SecurityUtils securityUtils
    ) {
        this.commentService = commentService;
        this.securityUtils  = securityUtils;
    }

    /** Create a comment. user is taken from JWT */
    @PostMapping
    public Comment create(@RequestBody Comment comment) {
        ApplicationUser user = securityUtils.getCurrentUser();
        comment.setUser(user);
        return commentService.create(comment);
    }

    /** Get a single comment */
    @GetMapping("/{id}")
    public Comment getById(@PathVariable UUID id) {
        return commentService.getById(id);
    }

    /** Delete a comment by id */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        commentService.delete(id);
    }

    /** List only the comments for one artwork */
    @GetMapping
    public List<Comment> getByTarget(
            @RequestParam UUID targetId,
            @RequestParam InteractionTargetType targetType
    ) {
        return commentService.findByTarget(targetId, targetType);
    }
}
