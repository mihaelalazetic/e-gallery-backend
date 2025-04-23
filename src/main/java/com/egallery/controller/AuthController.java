package com.egallery.controller;
// src/main/java/com/egallery/controller/AuthController.java

import com.egallery.model.dto.ApplicationUserDTO;
import com.egallery.model.dto.JwtAuthResponse;
import com.egallery.model.dto.LoginRequest;
import com.egallery.model.dto.SignupRequest;
import com.egallery.model.entity.ApplicationUser;
import com.egallery.model.entity.Role;
import com.egallery.model.entity.RoleName;
import com.egallery.repository.ApplicationUserRepository;
import com.egallery.repository.RoleRepository;
import com.egallery.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ApplicationUserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest request) {
        if (userRepo.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email already in use");
        }
        if (userRepo.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username already in use");
        }
        if (!Objects.equals(request.getPassword(), request.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Passwords do not match");
        }

        ApplicationUser user = new ApplicationUser();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setSlug(request.getUsername().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setBio(request.getBio());
        user.setFullName(request.getFullName());
        user.setProfilePictureUrl(request.getProfilePictureUrl());

        Set<Role> roles = new HashSet<>();
        if (request.getRole() != null) {
            Role role = roleRepo.findByName(RoleName.valueOf(request.getRole().toUpperCase()))
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            roles.add(role);
        } else {
            roles.add(roleRepo.findByName(RoleName.USER).orElseThrow());
        }
        user.setRoles(roles);

        userRepo.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            // load full user
            ApplicationUser user = userRepo.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String token = jwtTokenProvider.generateToken(user.getUsername());

            JwtAuthResponse resp = new JwtAuthResponse(token, jwtExpirationInMs);
            return ResponseEntity.ok(resp);

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    /**
     * Returns the currently authenticated user.
     */
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApplicationUserDTO> getCurrentUser(
            Authentication authentication   // Spring injects this for you
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 1) Get the username from the authentication token
        String username = authentication.getName();

        // 2) Load the full ApplicationUser entity
        ApplicationUser user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3) Map to DTO (your existing helper)
        ApplicationUserDTO dto = user.mapToDto();

        return ResponseEntity.ok(dto);
    }
}
