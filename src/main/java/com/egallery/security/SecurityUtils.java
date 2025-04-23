package com.egallery.security;


import com.egallery.repository.ApplicationUserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.egallery.model.entity.ApplicationUser;

public class SecurityUtils {

    private static ApplicationUserRepository staticUserRepo;

    public static void init(ApplicationUserRepository userRepository) {
        staticUserRepo = userRepository;
    }

    public static ApplicationUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Not authenticated");
        }
        String username = authentication.getName();
        return staticUserRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

}
