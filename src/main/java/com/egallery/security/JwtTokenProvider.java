package com.egallery.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
public class JwtTokenProvider {


    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    @Value("${jwt.secret}")
    private String jwtSecret;

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        String token = Jwts.builder().setSubject(username).setIssuedAt(now).setExpiration(expiryDate).signWith(getSigningKey(), SignatureAlgorithm.HS512).compact();

        log.debug("Generated JWT: {}", token);
        return token;

    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            if (authToken.split("\\.").length != 3) {
                log.error("Invalid JWT format: expected 3 parts but got {}", authToken.split("\\.").length);
                return false;
            }

            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(authToken); // Using same signing key

            return true;
        } catch (JwtException ex) {
            log.error("Invalid JWT token: {}", ex.getMessage());  // Add logging if needed
            return false;
        }
    }

}
