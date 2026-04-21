package com.techlab.configuration;

import com.techlab.service.IJwtService;
import com.techlab.service.ILogoutService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final IJwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ILogoutService logoutService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        try {
            final String token = getTokenFromRequest(request);
            
            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // Check if token is blacklisted (user logged out)
            if (logoutService.isTokenInvalidated(token)) {
                log.debug("Token is blacklisted (user logged out)");
                filterChain.doFilter(request, response);
                return;
            }

            // Get username from token
            final String username = jwtService.getUsernameFromToken(token);
            
            // Also try to get userId directly from token
            final Long userIdFromToken = jwtService.getUserIdFromToken(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Standard flow: validate via username
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                if (jwtService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userIdFromToken != null ? userIdFromToken : userDetails,
                            null, 
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.debug("Authenticated user: {}, userId: {}", username, userIdFromToken);
                } else {
                    log.debug("Token validation failed for user: {}", username);
                }
            } else if (userIdFromToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Fallback: if username not available but userId is, try to load user by ID
                try {
                    log.debug("Token has userId but no username claim - authentication not set");
                } catch (Exception e) {
                    log.warn("Could not authenticate with userId only: {}", e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());
            // Don't block request - Spring Security will handle based on endpoint config
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
            return authHeader.substring(7);
        }

        return null;
    }


}