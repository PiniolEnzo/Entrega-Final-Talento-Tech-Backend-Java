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
            // 1. Extraer token del header Authorization
            final String token = getTokenFromRequest(request);
            
            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // 2. Verificar si el token está en blacklist (logout)
            if (logoutService.isTokenInvalidated(token)) {
                log.debug("Token is blacklisted - user logged out");
                filterChain.doFilter(request, response);
                return;
            }

            // 3. Extraer email del token (subject)
            final String email = jwtService.getUsernameFromToken(token);
            
            if (email == null) {
                log.debug("Token does not contain valid email");
                filterChain.doFilter(request, response);
                return;
            }

            // 4. Si no hay autenticación, cargar usuario por email y validar
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                // Cargar usuario por email (ahora email es el username para Spring Security)
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                
                // Validar token contra el usuario
                if (jwtService.isTokenValid(token, userDetails)) {
                    // Obtener userId del token
                    Long userId = jwtService.getUserIdFromToken(token);
                    
                    // Crear autenticación con userId como principal
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userId != null ? userId : userDetails,
                            null, 
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.debug("Authenticated user: {} (userId: {})", email, userId);
                } else {
                    log.debug("Token validation failed for email: {}", email);
                }
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());
            // No bloqueamos el request - Spring Security maneja según config
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }

}