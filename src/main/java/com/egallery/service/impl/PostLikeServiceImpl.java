package com.egallery.service.impl;

import com.egallery.model.entity.PostLike;
import com.egallery.model.entity.ApplicationUser;
import com.egallery.model.entity.InteractionTargetType;
import com.egallery.repository.PostLikeRepository;
import com.egallery.service.PostLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostLikeServiceImpl implements PostLikeService {

    @Autowired
    private PostLikeRepository likeRepository;

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

    @Override
    public Long likePost(ApplicationUser user, UUID postId,InteractionTargetType targetType) {
        // Check if the like already exists for the user and this post
        Optional<PostLike> existingLike = likeRepository.findByApplicationUserAndTargetIdAndTargetType(user, postId, targetType);
//        if(existingLike.isPresent()){
//            // Optionally, you can return the existing like or throw an exception.
//            return existingLike.get();
//        }

        // Create and save a new PostLike
        PostLike newLike = new PostLike();
        newLike.setApplicationUser(user);
        newLike.setTargetId(postId);
        newLike.setTargetType(targetType);likeRepository.save(newLike);
        return likeRepository.countAllByTargetId(postId);
    }
}
