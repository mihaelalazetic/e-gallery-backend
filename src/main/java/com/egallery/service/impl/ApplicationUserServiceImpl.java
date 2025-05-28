
package com.egallery.service.impl;

import com.egallery.model.dto.ApplicationUserDTO;
import com.egallery.model.dto.ArtworkDto;
import com.egallery.model.entity.ApplicationUser;
import com.egallery.repository.ApplicationUserRepository;
import com.egallery.security.SecurityUtils;
import com.egallery.service.ApplicationUserService;
import com.egallery.service.ArtworkService;
import com.egallery.service.SubscriptionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApplicationUserServiceImpl implements ApplicationUserService {


    private final ApplicationUserRepository userRepository;
    private final SubscriptionService subscriptionService;
    private final ArtworkService artworkService;
    private final SecurityUtils securityUtils;

    public ApplicationUserServiceImpl(ApplicationUserRepository userRepository, SubscriptionService subscriptionService, ArtworkService artworkService, SecurityUtils securityUtils) {
        this.userRepository = userRepository;
        this.subscriptionService = subscriptionService;
        this.artworkService = artworkService;
        this.securityUtils = securityUtils;
    }

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
        ApplicationUser currentUser = securityUtils.getCurrentUser();

        for (Object[] row : users) {
            // Assuming row[0] = user id, row[1] = username, row[2] = total likes count
            UUID userId = (UUID) row[0];
            String username = (String) row[1];
            Long totalLikes = ((Number) row[2]).longValue();
            Optional<ApplicationUser> user = userRepository.findById(userId);

            Long followers = subscriptionService.countSubscribers(userId);
            boolean following = user.isPresent()
                    && subscriptionService.isSubscribed(currentUser, userId);
            Long artCount = artworkService.countByUserId(userId);

            user.ifPresent(applicationUser -> dtos.add(applicationUser.mapToDto(totalLikes, followers, following, artCount)));
        }

        return dtos;
    }

    @Override
    public Object getBySlug(String slug) {
        return userRepository.findBySlug(slug)
                .map(user -> {
                    Long followers = subscriptionService.countSubscribers(user.getId());
                    boolean following = subscriptionService.isSubscribed(securityUtils.getCurrentUser(), user.getId());
                    Long artCount = artworkService.countByUserId(user.getId());
                    ApplicationUserDTO applicationUserDTO = user.mapToDto(0L, followers, following, artCount);

                    List<ArtworkDto> artworkDtos = artworkService.findByArtistId(user.getId()).stream().map(artwork -> artwork.toDto(securityUtils.getCurrentUser())).toList();
                    applicationUserDTO.setArtworks(artworkDtos);
                    return applicationUserDTO;
                })
                .orElse(null);
    }

}
