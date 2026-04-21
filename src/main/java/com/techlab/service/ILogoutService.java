package com.techlab.service;

import java.util.Set;

public interface ILogoutService {
    /**
     * Add token to blacklist
     * @param token JWT token to invalidate
     * @param expirationTime token expiration timestamp in millis
     */
    void invalidateToken(String token, long expirationTime);
    
    /**
     * Check if token is blacklisted
     * @param token JWT token to check
     * @return true if token is blacklisted
     */
    boolean isTokenInvalidated(String token);
}
