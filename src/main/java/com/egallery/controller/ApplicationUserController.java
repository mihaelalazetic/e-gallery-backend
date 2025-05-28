
package com.egallery.controller;

import com.egallery.model.dto.ApplicationUserDTO;
import com.egallery.model.dto.UpdateUserRequestDTO;
import com.egallery.model.entity.ApplicationUser;
import com.egallery.security.SecurityUtils;
import com.egallery.service.ApplicationUserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@SecurityRequirement(name = "bearer-jwt")
@RestController
@RequestMapping("/api/users")
public class ApplicationUserController {


    private final ApplicationUserService userService;

    private final SecurityUtils securityUtils;

    public ApplicationUserController(ApplicationUserService userService, SecurityUtils securityUtils) {
        this.userService = userService;
        this.securityUtils = securityUtils;
    }
    @PostMapping
    public ApplicationUser create(@RequestBody ApplicationUser entity) {
        return userService.create(entity);
    }

    @GetMapping("/{id}")
    public ApplicationUser getById(@PathVariable UUID id) {
        return userService.getById(id);
    }

    @GetMapping
    public List<ApplicationUser> getAll() {
        return userService.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        userService.delete(id);
    }

    @GetMapping("/getCurrentUser")
    public ApplicationUserDTO getCurrentUser() {
        return securityUtils.getCurrentUser().mapToDto();
    }

    @GetMapping("/most-liked-artists")
    public ResponseEntity<List<ApplicationUserDTO>> getMostLikedArtists() {
        return ResponseEntity.ok(userService.getMostLikedArtists());
    }

    @GetMapping("/get-by-slug/{slug}")
    public ResponseEntity<Object> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(userService.getBySlug(slug));
    }


    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequestDTO request) {
        ApplicationUser user = userService.getById(request.getId());

        user.setBio(request.getBio());
        user.setFullName(request.getFullName());
        user.setProfilePictureUrl(request.getProfilePictureUrl());

        userService.create(user);
        return ResponseEntity.ok("User updated successfully");
    }


}
