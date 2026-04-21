package com.techlab.service.implementation;

import com.techlab.service.ILogoutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class LogoutServiceImpl implements ILogoutService {
    
    // In-memory token blacklist (for production, use Redis or database)
    private final ConcurrentHashMap<String, Long> tokenBlacklist = new ConcurrentHashMap<>();
    
    // Cleanup expired entries every 5 minutes
    private final ScheduledExecutorService cleanupExecutor = Executors.newSingleThreadScheduledExecutor();
    
    public LogoutServiceImpl() {
        // Cleanup task to remove expired tokens from blacklist
        cleanupExecutor.scheduleAtFixedRate(this::cleanupExpiredTokens, 5, 5, TimeUnit.MINUTES);
    }
    
    @Override
    public void invalidateToken(String token, long expirationTime) {
        tokenBlacklist.put(token, expirationTime);
        log.info("Token invalidated, will expire at: {}", Instant.ofEpochMilli(expirationTime));
    }
    
    @Override
    public boolean isTokenInvalidated(String token) {
        Long expiration = tokenBlacklist.get(token);
        if (expiration == null) {
            return false;
        }
        
        // If token has expired, remove from blacklist and return false
        if (System.currentTimeMillis() > expiration) {
            tokenBlacklist.remove(token);
            return false;
        }
        
        return true;
    }
    
    /**
     * Remove expired tokens from blacklist to prevent memory leak
     */
    private void cleanupExpiredTokens() {
        long now = System.currentTimeMillis();
        int beforeSize = tokenBlacklist.size();
        
        tokenBlacklist.entrySet().removeIf(entry -> entry.getValue() < now);
        
        int removed = beforeSize - tokenBlacklist.size();
        if (removed > 0) {
            log.debug("Cleaned up {} expired tokens from blacklist", removed);
        }
    }
    
    /**
     * Get current blacklist size (useful for monitoring)
     */
    public int getBlacklistSize() {
        return tokenBlacklist.size();
    }
}