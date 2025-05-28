
package com.egallery.repository;

import com.egallery.model.entity.Artwork;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ArtworkRepository extends JpaRepository<Artwork, UUID>, JpaSpecificationExecutor<Artwork>{
@Query(
            value = """
      SELECT a
      FROM Artwork a
      LEFT JOIN a.likes l
      GROUP BY a
      ORDER BY COUNT(l) DESC
      """)
    Page<Artwork> findAllOrderByLikesDesc(Pageable pageable);

    Long countByArtistId(UUID userId);

    List<Artwork> findByArtistId(UUID userId);

    @Query("""
        SELECT a FROM Artwork a
        LEFT JOIN a.likes l
        WHERE a.artist.id = :artistId
        GROUP BY a
        ORDER BY COUNT(l) DESC
        """)
    List<Artwork> findTopByArtistOrderByLikesDesc(
            @Param("artistId") UUID artistId, Pageable pageable
    );

    Page<Artwork> findAll(Pageable pageable);
}
