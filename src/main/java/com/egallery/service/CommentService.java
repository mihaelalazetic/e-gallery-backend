
package com.egallery.service;

import com.egallery.model.entity.Comment;
import com.egallery.model.enums.InteractionTargetType;

import java.util.List;
import java.util.UUID;

public interface CommentService {
    Comment create(Comment entity);
    Comment getById(UUID id);
    List<Comment> getAll();
    void delete(UUID id);

    List<Comment> findByTarget(UUID targetId, InteractionTargetType targetType);
}
