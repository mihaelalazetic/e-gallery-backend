
package com.egallery.repository;

import com.egallery.model.entity.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ExhibitionRepository extends JpaRepository<Exhibition, UUID> {
}
