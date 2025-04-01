
package com.egallery.service;

import com.egallery.model.dto.ApplicationUserDTO;
import com.egallery.model.entity.ApplicationUser;

import java.util.List;
import java.util.UUID;

public interface ApplicationUserService {
    ApplicationUser create(ApplicationUser entity);
    ApplicationUser getById(UUID id);
    List<ApplicationUser> getAll();
    void delete(UUID id);
    List<ApplicationUserDTO> getMostLikedArtists();
}
