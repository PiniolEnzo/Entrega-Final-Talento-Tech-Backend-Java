package com.techlab.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public interface IJwtService {
    String getToken(UserDetails user);

    String getUsernameFromToken(String token);

    boolean isTokenValid(String token, UserDetails userDetails);

    Long extractUserId(String token);
    
    /**
     * Get user ID from token without requiring UserDetails validation
     * @param token JWT token string
     * @return user ID if present, null otherwise
     */
    Long getUserIdFromToken(String token);
    
    /**
     * Get token expiration date
     * @param token JWT token string
     * @return expiration Date, or null if invalid
     */
    Date getTokenExpiration(String token);
}
