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
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

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
                .headers(headers -> headers
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("default-src 'self'; " +
                                        "script-src 'self'; " +
                                        "style-src 'self' https://fonts.googleapis.com; " +
                                        "font-src https://fonts.gstatic.com; " +
                                        "img-src 'self' https://placehold.co https://talento-tech-production.up.railway.app data:;" +
                                        "connect-src 'self' https://talento-tech-production.up.railway.app; " +
                                        "base-uri 'self'; " +
                                        "form-action 'self'; "
                                )
                        )
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                        .referrerPolicy(rp -> rp.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                        .httpStrictTransportSecurity(hsts -> hsts
                                .includeSubDomains(true)
                                .maxAgeInSeconds(31536000)
                        )
                        .contentTypeOptions(Customizer.withDefaults())
                        .permissionsPolicyHeader(pp -> pp
                                .policy("geolocation=(), microphone=(), camera=()")
                        )
                )
                .authorizeHttpRequests(authRequest ->
                        authRequest
                                // ============ AUTH ============
                                .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                                .requestMatchers(HttpMethod.POST, "/auth/logout").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                                .requestMatchers(HttpMethod.POST, "/auth/forgot-password").permitAll()
                                .requestMatchers(HttpMethod.GET, "/auth/validate").permitAll()
                                .requestMatchers(HttpMethod.POST, "/auth/reset-password").permitAll()
                                .requestMatchers(HttpMethod.POST, "/auth/change-password").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                                .requestMatchers(HttpMethod.GET, "/auth/me").hasAnyRole(Role.ADMIN.name(), Role.USER.name())

                                // ============ CARTS ============
                                .requestMatchers("/carts/**").hasRole(Role.USER.name())

                                // ============ CATEGORIES ============
                                .requestMatchers(HttpMethod.GET, "/categories").permitAll()
                                .requestMatchers(HttpMethod.GET, "/categories/all").hasRole(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.GET, "/categories/*").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                                .requestMatchers(HttpMethod.POST, "/categories").hasRole(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.PUT, "/categories/*").hasRole(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.DELETE, "/categories/*").hasRole(Role.ADMIN.name())

                                // ============ ORDERS ============
                                .requestMatchers(HttpMethod.GET, "/orders/my-orders").hasRole(Role.USER.name())
                                .requestMatchers(HttpMethod.POST, "/orders/**").hasRole(Role.USER.name())
                                .requestMatchers("/orders/**").hasRole(Role.ADMIN.name())


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