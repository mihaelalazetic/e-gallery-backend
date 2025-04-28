// src/main/java/com/egallery/service/impl/CommentServiceImpl.java

package com.egallery.service.impl;

import com.egallery.model.entity.Comment;
import com.egallery.model.entity.InteractionTargetType;
import com.egallery.repository.CommentRepository;
import com.egallery.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment create(Comment entity) {
        return commentRepository.save(entity);
    }

    @Override
    public Comment getById(UUID id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    @Override
    public void delete(UUID id) {
        commentRepository.deleteById(id);
    }

    @Override
    public List<Comment> findByTarget(UUID targetId, InteractionTargetType targetType) {
        return commentRepository
                .findAllByTargetIdAndTargetTypeAndIsVisibleTrueOrderByCreatedAtAsc(
                        targetId, targetType
                );
    }
}
