package com.example.project.Config;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

import java.util.Date;
import java.util.function.Function;


@Component //Jwt utility class for generating and validating JWT tokens
public class JwtUtil {
    private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Secure random key for signing JWT tokens
    private final long JWT_EXPIRATION = 86400000; // 24 hours in milliseconds

    public String generateToken(String username) {
        return Jwts.builder()                                                    // Start building JWT
                .setSubject(username)                                           // Set username as "subject"
                .setIssuedAt(new Date())                                       // Set current time as "issued at"
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION)) // Set expiration time
                .signWith(SECRET_KEY)                                          // Sign with secret key
                .compact();                                                    // Convert to string
    }

    public String extractUsername(String token) { // Extract username from JWT token
        return extractClaim(token, Claims::getSubject);
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);        // Parse token and get all claims
        return claimsResolver.apply(claims);                  // Apply the function to extract specific claim
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()               // Create JWT parser
                .setSigningKey(SECRET_KEY)        // Set the key to verify signature
                .build()                          // Build the parser
                .parseClaimsJws(token)           // Parse and verify the token
                .getBody();                      // Get the claims (payload)
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration); // Extract expiration date from token
    }
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); // Check if token expiration date is before current date
    }

    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);              // Get username from token
        return (extractedUsername.equals(username) && !isTokenExpired(token)); // Check username match + not expired
    }
}
