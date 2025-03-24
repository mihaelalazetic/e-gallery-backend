
package com.egallery.service;

import com.egallery.model.entity.Comment;
import java.util.List;
import java.util.UUID;

public interface CommentService {
    Comment create(Comment entity);
    Comment getById(UUID id);
    List<Comment> getAll();
    void delete(UUID id);
}
