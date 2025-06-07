
package com.egallery.repository;

import com.egallery.model.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VenueRepository extends JpaRepository<Venue, UUID> {
    Optional<Venue> findByAddressContainingIgnoreCase(String address);
    Optional<Venue> findByNameContainingIgnoreCase(String name);
}
