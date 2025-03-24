
package com.egallery.repository;

import com.egallery.model.entity.ArtType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ArtTypeRepository extends JpaRepository<ArtType, UUID> {
}
