package com.example.project.Config;

import org.springframework.stereotype.Component;
import com.example.project.Service.CustomUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization"); // Get Authorization header
        String username = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) { // Check for Bearer token
            jwt = authHeader.substring(7); // Extract token after "Bearer "
            try {
            username = jwtUtil.extractUsername(jwt); // Extract username from token
            } catch (Exception e) {
                logger.warn("JWT token extraction failed: " + e.getMessage());
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) { // If username found and no existing authentication
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if (jwtUtil.validateToken(jwt, userDetails.getUsername())) { // Validate token
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken( // Create auth token
                        userDetails, null, userDetails.getAuthorities()); // Set user details and authorities
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // Set request details
                SecurityContextHolder.getContext().setAuthentication(authToken); // Set authentication in security context
            }
        }
        filterChain.doFilter(request, response); // Continue filter chain
    }
}
