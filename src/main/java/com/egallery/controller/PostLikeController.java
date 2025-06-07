
    package com.egallery.controller;

    import com.egallery.model.entity.ApplicationUser;
    import com.egallery.model.enums.InteractionTargetType;
    import com.egallery.model.entity.PostLike;
    import com.egallery.security.SecurityUtils;
    import com.egallery.service.PostLikeService;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;
    import java.util.UUID;

    @RestController
    @RequestMapping("/api/likes")
    public class PostLikeController {

        private final PostLikeService likeService;
        private final SecurityUtils securityUtils;

        public PostLikeController(PostLikeService likeService, SecurityUtils securityUtils) {
            this.likeService = likeService;
            this.securityUtils = securityUtils;
        }

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

        @PostMapping("/like/{postId}/{targetType}")
        public Long likePost(@PathVariable UUID postId, @PathVariable String targetType) {
            ApplicationUser currentUser = securityUtils.getCurrentUser();
            InteractionTargetType type = InteractionTargetType.valueOf(targetType);
            return likeService.likePost(currentUser, postId, type);
        }

    }
