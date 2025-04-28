
package com.egallery.controller;

import com.egallery.model.dto.ApplicationUserDTO;
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

    @Autowired
    private ApplicationUserService userService;

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

}
