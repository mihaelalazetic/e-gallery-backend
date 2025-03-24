
package com.egallery.repository;

import com.egallery.model.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, UUID> {
    Optional<ApplicationUser> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<ApplicationUser> findByUsername(String username);
    boolean existsByUsername(String username);
}
