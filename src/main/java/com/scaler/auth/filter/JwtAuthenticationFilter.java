package com.scaler.auth.filter;

import com.scaler.auth.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.List;
import java.util.stream.Collectors;
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        log.info("Checking Authorization header: {}", authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            log.info("Extracted JWT token: {}", token);

            try {
                Claims claims = jwtUtil.extractClaims(token);
                log.info("Extracted Claims: {}", claims);

                String username = claims.getSubject();
                var roles = claims.get("roles", List.class);

                log.info("Username: {}", username);
                log.info("Roles from token: {}", roles);

                if (username != null && roles != null) {
                    var authorities = roles.stream()
                            .map(role -> new SimpleGrantedAuthority(role.toString()))
                            .toList();

                    log.info("Mapped authorities: {}", authorities);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    log.info("Authentication set for user '{}'", username);
                }

            } catch (Exception e) {
                log.error("JWT processing failed", e);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } else {
            log.info("No Bearer token found in Authorization header");
        }

        filterChain.doFilter(request, response);
    }
}
