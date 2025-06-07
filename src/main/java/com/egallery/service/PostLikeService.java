
package com.egallery.service;

import com.egallery.model.entity.ApplicationUser;
import com.egallery.model.enums.InteractionTargetType;
import com.egallery.model.entity.PostLike;

import java.util.List;
import java.util.UUID;

public interface PostLikeService {
    PostLike create(PostLike entity);
    PostLike getById(UUID id);
    List<PostLike> getAll();
    void delete(UUID id);
    Long likePost(ApplicationUser user, UUID postId, InteractionTargetType targetType);
}
