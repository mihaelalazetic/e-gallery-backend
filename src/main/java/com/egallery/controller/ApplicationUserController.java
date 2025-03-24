
package com.egallery.controller;

import com.egallery.model.entity.ApplicationUser;
import com.egallery.service.impl.ApplicationUserServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@SecurityRequirement(name = "bearer-jwt")
@RestController
@RequestMapping("/api/users")
public class ApplicationUserController {

    @Autowired
    private ApplicationUserServiceImpl userService;

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
}
