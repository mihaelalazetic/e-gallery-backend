
package com.egallery.repository;

import com.egallery.model.entity.ArtType;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.security.auth.spi.LoginModule;
import java.util.UUID;

public interface ArtTypeRepository extends JpaRepository<ArtType, Long> {
}
