
package com.egallery.config;

import com.egallery.model.entity.ApplicationUser;
import com.egallery.model.entity.Role;
import com.egallery.model.entity.RoleName;
import com.egallery.repository.ApplicationUserRepository;
import com.egallery.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class AdminUserConfig {

    @Bean
    public CommandLineRunner setupAdminUser(ApplicationUserRepository userRepo, RoleRepository roleRepo, PasswordEncoder passwordEncoder) {
        return args -> {
            if (roleRepo.findAll().isEmpty()) {
                for (RoleName roleName : RoleName.values()) {
                    Role role = new Role();
                    role.setName(roleName);
                    role.setDescription("Role: " + roleName.name());
                    roleRepo.save(role);
                }
            }

            if (userRepo.findAll().isEmpty()) {
                ApplicationUser admin = new ApplicationUser();
                admin.setUsername("admin");
                admin.setUsername("Admin Admin");
                admin.setEmail("admin@egallery.com");
                admin.setPassword(passwordEncoder.encode("123"));
                admin.setSlug("admin");
                admin.setRoles(Set.of(roleRepo.findByName(RoleName.ADMIN).orElseThrow()));
                userRepo.save(admin);
            }
        };
    }
}
