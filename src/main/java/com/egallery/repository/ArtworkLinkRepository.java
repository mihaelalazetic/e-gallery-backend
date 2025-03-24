
package com.egallery.repository;

import com.egallery.model.entity.ArtworkLink;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ArtworkLinkRepository extends JpaRepository<ArtworkLink, UUID> {
}
