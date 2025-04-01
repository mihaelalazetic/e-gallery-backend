
package com.egallery.repository;

import com.egallery.model.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, UUID> {
    Optional<ApplicationUser> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<ApplicationUser> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query(value = """
                SELECT u.id AS artistId, u.username, u.avatar_url,
                       COALESCE(SUM(CASE WHEN pl.target_type = 'ARTWORK' THEN 1 ELSE 0 END), 0) +
                       COALESCE(SUM(CASE WHEN pl.target_type = 'EXHIBITION' THEN 1 ELSE 0 END), 0) AS total_likes
                FROM application_user u
                LEFT JOIN artwork a ON a.artist_id = u.id
                LEFT JOIN exhibition e ON e.artist_id = u.id
                LEFT JOIN post_like pl ON (
                    (pl.target_type = 'ARTWORK' AND pl.target_id = a.id) OR
                    (pl.target_type = 'EXHIBITION' AND pl.target_id = e.id)
                )
                GROUP BY u.id, u.username, u.avatar_url
                ORDER BY total_likes DESC
                LIMIT 6
            """, nativeQuery = true)
    List<Object[]> findTopFeaturedArtists();

}
