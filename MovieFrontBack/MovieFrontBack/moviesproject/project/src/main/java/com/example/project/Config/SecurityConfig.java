package com.example.project.Config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;




@Configuration
@EnableWebSecurity // Enable Spring Security for the application
@EnableMethodSecurity(prePostEnabled = true) // Enable method-level security
/// Configuration class to define security-related beans
public class SecurityConfig {


    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    //PasswordEncoder bean using BCrypt hashing algorithm
    @Bean   
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean 
    public AuthenticationManager authenticationManager (AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*")); // Allow all origins for development
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { 
        http 
        .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS
        .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity
            .sessionManagement(session -> session .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
            .requestMatchers("/register", "/login").permitAll() // Allow unauthenticated access to /register and /login
            .requestMatchers("/movie", "/movie/**").permitAll() // Allow unauthenticated access to movie endpoints
            .requestMatchers("/reviews/**").hasAnyRole("USER", "ADMIN", "MODERATOR") // Reviews require authentication
            .requestMatchers("/admin/**").hasRole("ADMIN") // Admin endpoints only for ADMIN role
            .requestMatchers("/moderator/**").hasAnyRole("ADMIN", "MODERATOR") // Moderator endpoints for ADMIN and MODERATOR
            .anyRequest().authenticated()
            )
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter before the username-password authentication filter (allows user to stay logged in)

        return http.build();
    }


}
