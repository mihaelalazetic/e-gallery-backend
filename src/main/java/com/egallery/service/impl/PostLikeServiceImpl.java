package com.egallery.service.impl;

import com.egallery.model.entity.PostLike;
import com.egallery.model.entity.ApplicationUser;
import com.egallery.model.enums.InteractionTargetType;
import com.egallery.repository.PostLikeRepository;
import com.egallery.service.PostLikeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeRepository likeRepository;

    public PostLikeServiceImpl(PostLikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    @Override
    public PostLike create(PostLike entity) {
        return likeRepository.save(entity);
    }

    @Override
    public PostLike getById(UUID id) {
        return likeRepository.findById(id).orElse(null);
    }

    @Override
    public List<PostLike> getAll() {
        return likeRepository.findAll();
    }

    @Override
    public void delete(UUID id) {
        likeRepository.deleteById(id);
    }

    /**
     * Toggle a like: if already liked → unlike; else → like.
     * Returns the new total likes count.
     */
    @Override
    @Transactional
    public Long likePost(ApplicationUser user, UUID postId, InteractionTargetType targetType) {
        Optional<PostLike> existing = likeRepository
                .findByApplicationUserAndTargetIdAndTargetType(user, postId, targetType);

        if (existing.isPresent()) {
            // Unlike
            likeRepository.delete(existing.get());
        } else {
            // Like
            PostLike newLike = new PostLike();
            newLike.setApplicationUser(user);
            newLike.setTargetId(postId);
            newLike.setTargetType(targetType);
            likeRepository.save(newLike);
        }

        return likeRepository.countAllByTargetId(postId);
    }
}
