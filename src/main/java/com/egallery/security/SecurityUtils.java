// src/main/java/com/egallery/security/SecurityUtils.java
package com.egallery.security;

import com.egallery.model.entity.ApplicationUser;
import com.egallery.repository.ApplicationUserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Component
public class SecurityUtils {
    private final ApplicationUserRepository userRepository;

    public SecurityUtils(ApplicationUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Return the currently authenticated user, or null if none.
     */
    public ApplicationUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null
                || !auth.isAuthenticated()
                || auth instanceof AnonymousAuthenticationToken) {
            return null;    // anonymous
        }
        String username = auth.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public ApplicationUser getCurrentUser2() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null
                || !auth.isAuthenticated()
                || auth instanceof AnonymousAuthenticationToken) {
            return null;    // anonymous
        }
        String username = auth.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

}
