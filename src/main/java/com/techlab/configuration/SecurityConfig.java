package com.techlab.configuration;

import com.techlab.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authRequest ->
                        authRequest
                                // ============ AUTH ============
                                .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                                .requestMatchers(HttpMethod.POST, "/auth/logout").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                                .requestMatchers(HttpMethod.POST, "/auth/forgot-password").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                                .requestMatchers(HttpMethod.POST, "/auth/validate").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                                .requestMatchers(HttpMethod.POST, "/auth/change-password").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                                .requestMatchers(HttpMethod.GET, "/auth/me").hasAnyRole(Role.ADMIN.name(), Role.USER.name())

                                // ============ CARTS ============
                                .requestMatchers(HttpMethod.POST, "/carts").hasRole(Role.USER.name())
                                .requestMatchers(HttpMethod.POST, "/carts/*/items").hasRole(Role.USER.name())
                                .requestMatchers(HttpMethod.GET, "/carts/*").hasRole(Role.USER.name())
                                .requestMatchers(HttpMethod.PUT, "/carts/*/items/*").hasRole(Role.USER.name())
                                .requestMatchers(HttpMethod.DELETE, "/carts/*/items/*").hasRole(Role.USER.name())
                                .requestMatchers(HttpMethod.DELETE, "/carts/*/items").hasRole(Role.USER.name())

                                // ============ CATEGORIES ============
                                .requestMatchers(HttpMethod.GET, "/categories").permitAll()
                                .requestMatchers(HttpMethod.GET, "/categories/all").hasRole(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.GET, "/categories/*").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                                .requestMatchers(HttpMethod.POST, "/categories").hasRole(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.PUT, "/categories/*").hasRole(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.DELETE, "/categories/*").hasRole(Role.ADMIN.name())

                                // ============ ORDERS ============
                                .requestMatchers(HttpMethod.GET, "/orders/my-orders").hasRole(Role.USER.name())
                                .requestMatchers(HttpMethod.GET, "/orders").hasRole(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.GET, "/orders/*").hasRole(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.PUT, "/orders/*/status").hasRole(Role.ADMIN.name())

                                // ============ PRODUCTS ============
                                .requestMatchers(HttpMethod.GET, "/products").permitAll()
                                .requestMatchers(HttpMethod.GET, "/products/*").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                                .requestMatchers(HttpMethod.POST, "/products").hasRole(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.PUT, "/products/*").hasRole(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.DELETE, "/products/*").hasRole(Role.ADMIN.name())

                                // ============ USERS ============
                                .requestMatchers(HttpMethod.GET, "/users").hasRole(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.GET, "/users/*").hasRole(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.PUT, "/users/*").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                                .requestMatchers(HttpMethod.DELETE, "/users/*").hasRole(Role.ADMIN.name())

                                // ============ SWAGGER ============
                                .requestMatchers("/swagger-ui/**").hasRole(Role.ADMIN.name())
                                .requestMatchers("/swagger-ui.html").hasRole(Role.ADMIN.name())
                                .requestMatchers("/v3/api-docs/**").hasRole(Role.ADMIN.name())

                                // ============ FALLBACK ============
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}