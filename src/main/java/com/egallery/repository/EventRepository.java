
package com.egallery.repository;

import com.egallery.model.entity.ApplicationUser;
import com.egallery.model.entity.Event;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    List<Event> findAllByStartDateAfterAndIsPublicTrue(LocalDateTime startDate);
    List<Event> findAllByCreatedBy(ApplicationUser user);
    Optional<Event> findBySlug(String slug);
}
