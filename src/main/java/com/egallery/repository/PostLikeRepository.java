package com.egallery.repository;

import com.egallery.model.entity.ApplicationUser;
import com.egallery.model.enums.InteractionTargetType;
import com.egallery.model.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostLikeRepository extends JpaRepository<PostLike, UUID> {
    @Query("""
            SELECT pl.targetId, COUNT(pl)
            FROM PostLike pl
            WHERE pl.targetType = :targetType
            GROUP BY pl.targetId
            """)
    List<Object[]> countLikesByTargetType(@Param("targetType") InteractionTargetType targetType);

    Optional<PostLike> findByApplicationUserAndTargetIdAndTargetType(ApplicationUser applicationUser, UUID targetId, InteractionTargetType targetType);

    Long countAllByTargetId(UUID targetId);
}
