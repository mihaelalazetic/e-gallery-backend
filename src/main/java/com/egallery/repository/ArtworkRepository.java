
package com.egallery.repository;

import com.egallery.model.entity.Artwork;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ArtworkRepository extends JpaRepository<Artwork, UUID> {
}
