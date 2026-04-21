package com.techlab.service.implementation;

import com.techlab.entity.User;
import com.techlab.repository.IUserRepository;
import com.techlab.service.IJwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service("jwtService")
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements IJwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;
    
    private final IUserRepository userRepository;

    @Override
    public String getToken(UserDetails user) {
        // Find user by username to get their ID
        Optional<User> userOpt = userRepository.findByName(user.getUsername());
        Long userId = userOpt.map(User::getId).orElse(null);
        
        return generateToken(new HashMap<>(), user.getUsername(), userId);
    }

    @Override
    public String getUsernameFromToken(String token) {
        try {
            Claims claims = getAllClaims(token);
            // Prefer username from claim, fallback to subject
            String username = claims.get("username", String.class);
            if (username != null && !username.isEmpty()) {
                return username;
            }
            // If no username claim, try to get from subject (but subject is userId now)
            // So we need to look up user by userId
            String subject = claims.getSubject();
            if (subject != null && subject.matches("\\d+")) {
                // Subject is userId, can't get username directly
                // This shouldn't happen with new tokens, but handle legacy
                return null;
            }
            return subject;
        } catch (Exception e) {
            log.error("Error extracting username from token: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = getUsernameFromToken(token);
            // If we couldn't get username from token, fall back to extracting userId
            // and comparing with UserDetails
            if (username == null) {
                // Try to validate using userId in subject
                Long userIdFromToken = extractUserId(token);
                if (userIdFromToken != null && userDetails instanceof User) {
                    return userIdFromToken.equals(((User) userDetails).getId());
                }
                return false;
            }
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            log.error("Token validation error: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public Long extractUserId(String token) {
        return getUserIdFromToken(token);
    }

    @Override
    public Long getUserIdFromToken(String token) {
        try {
            Claims claims = getAllClaims(token);
            
            // First try: get from "userId" claim
            Object userIdObj = claims.get("userId");
            if (userIdObj != null) {
                if (userIdObj instanceof Long) {
                    return (Long) userIdObj;
                } else if (userIdObj instanceof Integer) {
                    return ((Integer) userIdObj).longValue();
                } else if (userIdObj instanceof String) {
                    try {
                        return Long.parseLong((String) userIdObj);
                    } catch (NumberFormatException e) {
                        log.warn("Could not parse userId from String: {}", userIdObj);
                    }
                }
            }
            
            // Second try: parse from subject (which contains userId as string)
            String subject = claims.getSubject();
            if (subject != null && subject.matches("\\d+")) {
                return Long.parseLong(subject);
            }
            
            return null;
        } catch (Exception e) {
            log.error("Error extracting userId from token: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Generate JWT token with username as subject and userId in claims
     */
    private String generateToken(Map<String, Object> extraClaims, String username, Long userId) {
        Map<String, Object> claims = new HashMap<>(extraClaims);
        claims.put("username", username);
        if (userId != null) {
            claims.put("userId", userId);
        }
        
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(userId != null ? userId.toString() : username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getKey())
                .compact();
    }

    private Claims getAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Date getExpiration(String token){
        try {
            return getClaim(token, Claims::getExpiration);
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isTokenExpired(String token){
        try {
            Date expiration = getExpiration(token);
            return expiration != null && expiration.before(new Date());
        } catch (Exception e) {
            return true; // Consider expired if we can't check
        }
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver){
        try {
            final Claims claims = getAllClaims(token);
            return claimsResolver.apply(claims);
        } catch (Exception e) {
            log.error("Error getting claim from token: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public Date getTokenExpiration(String token) {
        return getExpiration(token);
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}