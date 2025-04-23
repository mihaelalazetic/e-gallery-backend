
package com.egallery.repository;

import com.egallery.model.entity.Artwork;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ArtworkRepository extends JpaRepository<Artwork, UUID> {
    @Query(
            value = """
      SELECT a
      FROM Artwork a
      LEFT JOIN a.likes l
      GROUP BY a
      ORDER BY COUNT(l) DESC
      """)
    Page<Artwork> findAllOrderByLikesDesc(Pageable pageable);
}
