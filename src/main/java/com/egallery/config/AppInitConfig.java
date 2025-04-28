package com.egallery.config;

import com.egallery.repository.ApplicationUserRepository;
import com.egallery.security.SecurityUtils;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.util.Base64;

@Configuration
@RequiredArgsConstructor
public class AppInitConfig {

    private final ApplicationUserRepository userRepository;

    @PostConstruct
    public void init() {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        System.out.println(Base64.getEncoder().encodeToString(key.getEncoded()));

        new SecurityUtils(userRepository);
    }
}
