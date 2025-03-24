
package com.egallery.repository;

import com.egallery.model.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface VenueRepository extends JpaRepository<Venue, UUID> {
}
