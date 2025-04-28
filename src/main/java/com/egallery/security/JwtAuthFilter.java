package com.egallery.security;

import com.egallery.repository.ApplicationUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;

    private final CustomUserDetailsService userDetailsService;

    private final ApplicationUserRepository userRepository;

    public JwtAuthFilter(JwtTokenProvider tokenProvider, CustomUserDetailsService userDetailsService, ApplicationUserRepository userRepository) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Bypass JWT processing for Swagger and authentication endpoints
        String servletPath = request.getServletPath();
        String method = request.getMethod();
        if (servletPath.startsWith("/swagger-ui") ||
                servletPath.startsWith("/v3/api-docs") ||
                servletPath.startsWith("/webjars") ||
                servletPath.startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        // 2) Public featured-art endpoint
//        if ("GET".equalsIgnoreCase(method) &&
//                "/api/artworks/featured".equals(servletPath)) {
//            filterChain.doFilter(request, response);
//            return;
//        }
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (tokenProvider.validateToken(token)) {
                var username = tokenProvider.getUsernameFromJWT(token);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
