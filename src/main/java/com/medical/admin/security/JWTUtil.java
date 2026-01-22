package com.medical.admin.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.Getter;

/**
 * JWT Utility class for generating, parsing, and validating JSON Web Tokens (JWT)
 * Used in Spring Boot authentication/authorization flow for stateless session management
 */
@Component
@Getter
public class JWTUtil {

    /**
     * JWT Secret Key injected from application.properties via @Value annotation
     * Must be at least 256 bits (32 bytes) for HS256 algorithm compliance (RFC 7518)
     */
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    /**
     * Effective signing key used for JWT operations
     * Set during @PostConstruct initialization to ensure HS256 compliance
     */
    private String effectiveSecret;

    /**
     * Initializes the JWT utility after Spring bean construction
     * Validates secret key length and sets effectiveSecret for signing operations
     * Called automatically by Spring after dependency injection
     */
    @PostConstruct
    public void init() {
        // HS256 requires minimum 256 bits (32 bytes). Base64-encoded 32 bytes = ~44 characters
        // Original error occurred because "mySecretKey123..." was only 40 chars (~240 bits)
        if (SECRET_KEY.length() < 44) {
            throw new IllegalArgumentException(
                "JWT secret too short for HS256 algorithm. " +
                "Minimum 256 bits required. Generate with: openssl rand -base64 32"
            );
        }
        // Use the injected secret directly (assumed to be Base64 or sufficiently long string)
        this.effectiveSecret = SECRET_KEY;
    }

    /**
     * Extracts username (subject) claim from JWT token
     * @param token Compact JWT string (Header.Payload.Signature)
     * @return username stored as subject claim
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Extracts expiration date from JWT token claims
     * @param token Compact JWT string
     * @return Date when token expires
     */
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    /**
     * Parses complete JWT token and extracts all claims from payload
     * Uses effectiveSecret for signature verification
     * @param token Compact JWT string to parse
     * @return Claims object containing all JWT payload data
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()                           // Create JWT parser
                .setSigningKey(effectiveSecret)        // Set secret key for signature verification
                .parseClaimsJws(token)                 // Parse and verify JWT, extract body
                .getBody();                            // Return claims payload
    }

    /**
     * Checks if JWT token has expired
     * Compares token expiration date with current system time
     * @param token JWT token to validate
     * @return true if token is expired, false otherwise
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Generates new JWT token for given username with default 10-hour expiration
     * Creates empty claims map (can be extended for roles/permissions)
     * @param username User identifier to embed as subject claim
     * @return Compact JWT string ready for HTTP Authorization header
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();   // Custom claims (roles, permissions, etc.)
        return createToken(claims, username);
    }

    /**
     * Core token creation method with customizable claims and subject
     * HS256 signing algorithm ensures token integrity and authenticity
     * @param claims Custom claims to embed in JWT payload
     * @param subject Primary identifier (usually username)
     * @return Signed compact JWT token
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()                                    // Start JWT builder
                .setClaims(claims)                               // Add custom claims to payload
                .setSubject(subject)                             // Set username as subject claim
                .setIssuedAt(new Date(System.currentTimeMillis())) // Set issuance timestamp (iat)
                .setExpiration(new Date(                         // Set expiration (exp) - 10 hours from now
                    System.currentTimeMillis() + 1000 * 60 * 60 * 10
                ))
                .signWith(SignatureAlgorithm.HS256, effectiveSecret) // Sign with HS256 + secret key
                .compact();                                      // Generate compact serialized token
    }

    /**
     * Validates JWT token for specific username
     * Checks: 1) Token signature valid, 2) Not expired, 3) Subject matches expected username
     * @param token JWT token from Authorization header
     * @param username Expected username from authentication request
     * @return true if token is valid for this user, false otherwise
     */
    public Boolean validateToken(String token, String username) {
        try {
            final String extractedUsername = extractUsername(token);  // Extract and verify subject
            return (extractedUsername.equals(username) && !isTokenExpired(token));
        } catch (Exception e) {
            // Token parsing failed (invalid signature, malformed, etc.)
            return false;
        }
    }
}
