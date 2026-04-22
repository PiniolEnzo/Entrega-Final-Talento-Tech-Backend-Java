package com.techlab.service.implementation;

import com.techlab.entity.User;
import com.techlab.repository.IUserRepository;
import com.techlab.service.IJwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
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
        // Find user by email to get their ID and name
        Optional<User> userOpt = userRepository.findByEmail(user.getUsername());
        Long userId = userOpt.map(User::getId).orElse(null);
        String name = userOpt.map(User::getName).orElse(user.getUsername());
        String email = user.getUsername();
        
        return generateToken(name, email, userId);
    }

    @Override
    public String getUsernameFromToken(String token) {
        try {
            Claims claims = getAllClaims(token);
            // Email is stored as subject for authentication
            return claims.getSubject();
        } catch (Exception e) {
            log.error("Error extracting username from token: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            String email = getUsernameFromToken(token);
            if (email == null) {
                return false;
            }
            return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
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
            
            return null;
        } catch (Exception e) {
            log.error("Error extracting userId from token: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Generate JWT token with email as subject (for login) and userId/name in claims
     */
    private String generateToken(String name, String email, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", name);
        claims.put("email", email);
        if (userId != null) {
            claims.put("userId", userId);
        }
        
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(email)  // Email as subject for authentication
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