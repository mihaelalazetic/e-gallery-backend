// src/main/java/com/egallery/repository/CommentRepository.java

package com.egallery.repository;

import com.egallery.model.entity.Comment;
import com.egallery.model.enums.InteractionTargetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findAllByTargetIdAndTargetTypeAndIsVisibleTrueOrderByCreatedAtAsc(
            UUID targetId,
            InteractionTargetType targetType
    );
}
