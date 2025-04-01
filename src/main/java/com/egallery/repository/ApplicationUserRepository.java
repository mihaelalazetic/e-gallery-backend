
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
            SELECT u.id, u.username, COUNT(pl.target_id) AS total_likes
            FROM application_user u
            JOIN artwork a ON a.artist_id = u.id
            LEFT JOIN post_like pl ON pl.target_type = 'ARTWORK' AND pl.target_id = a.id
            GROUP BY u.id, u.username
            ORDER BY total_likes DESC;
            """, nativeQuery = true)
    List<Object[]> findMostLikedArtists();



}
