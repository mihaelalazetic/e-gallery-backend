
package com.egallery.service.impl;

import com.egallery.model.entity.PostLike;
import com.egallery.repository.PostLikeRepository;
import com.egallery.service.PostLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
