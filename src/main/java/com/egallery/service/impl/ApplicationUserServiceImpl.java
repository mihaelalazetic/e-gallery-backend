
package com.egallery.service.impl;

import com.egallery.model.dto.ApplicationUserDTO;
import com.egallery.model.entity.ApplicationUser;
import com.egallery.repository.ApplicationUserRepository;
import com.egallery.service.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApplicationUserServiceImpl implements ApplicationUserService {

    @Autowired
    private ApplicationUserRepository userRepository;

    @Override
    public ApplicationUser create(ApplicationUser entity) {
        return userRepository.save(entity);
    }

    @Override
    public ApplicationUser getById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<ApplicationUser> getAll() {
        return userRepository.findAll();
    }

    @Override
    public void delete(UUID id) {
        userRepository.deleteById(id);
    }
    public List<ApplicationUserDTO> getMostLikedArtists() {
        List<Object[]> users = userRepository.findMostLikedArtists();
        List<ApplicationUserDTO> dtos = new ArrayList<>();

        for (Object[] row : users) {
            // Assuming row[0] = user id, row[1] = username, row[2] = total likes count
            UUID userId = (UUID) row[0];
            String username = (String) row[1];
            Long totalLikes = ((Number) row[2]).longValue();
            Optional<ApplicationUser> user = userRepository.findById(userId);
            ApplicationUserDTO dto;
            user.ifPresent(applicationUser -> dtos.add(applicationUser.mapToDto(totalLikes)));
        }

        return dtos;
    }

}
