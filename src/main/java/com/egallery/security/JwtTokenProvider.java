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
        // quick sanity check
        if (authToken == null || authToken.split("\\.").length != 3) {
            log.error("Invalid JWT format: expected 3 parts but got {}",
                    authToken == null ? null : authToken.split("\\.").length);
            return false;
        }

        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    // allow up to 60 seconds of clock skew
                    .setAllowedClockSkewSeconds(60)
                    .build()
                    .parseClaimsJws(authToken);
            return true;

        } catch (ExpiredJwtException ex) {
            // token is expired—but we handle it gracefully
            Date expiredAt = ex.getClaims().getExpiration();
            Date now = new Date();
            log.info("JWT expired at {}; current time {}", expiredAt, now);
            return false;

        } catch (JwtException ex) {
            // any other parsing/signature problems
            log.error("Invalid JWT token: {}", ex.getMessage());
            return false;
        }
    }

}
