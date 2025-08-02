package com.example.project.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers(
                    "/api/v1/auth/**",
                    "/api/auth/**",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/api-docs/**",
                    "/webjars/**",
                    // Maintenance endpoints
                    "/api/maintenance/**",
                    "/api/maintenance/schedule",
                    "/api/maintenance/*/status",
                    "/api/maintenance/terrain/**",
                    "/api/maintenance/history/**",
                    // Terrains endpoints
                    "/api/v1/terrains/**",
                    "/api/terrains/**",
                    // Événements endpoints (lecture seule publique)
                    "/api/evenements",
                    "/api/evenements/*",
                    "/api/equipes/evenement/*",
                    "/api/tarifications/evenement/*",
                    // Alertes endpoints (lecture seule publique)
                    "/api/alertes",
                    "/api/alertes/type/*",
                    "/api/alertes/urgent",
                    "/api/alertes/expired",
                    "/api/feedback/target/**",
                    "/api/feedback/stats/**"
                ).permitAll()
                
                // Endpoints protégés
                .requestMatchers(
                    "/api/evenements/**",
                    "/api/equipes/**",
                    "/api/participants/**",
                    "/api/tarifications/**",
                    // Gestion des alertes (écriture protégée)
                    "/api/alertes/**"
                ).authenticated()
                
                // Rôles spécifiques
                .requestMatchers(HttpMethod.POST, "/api/equipes").hasAnyRole("ADMIN", "ORGANIZER")
                .requestMatchers(HttpMethod.POST, "/api/alertes").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.PUT, "/api/alertes/**").hasAnyRole("ADMIN", "MANAGER")
                // POST feedback nécessite une authentification
                .requestMatchers(HttpMethod.POST, "/api/feedback").authenticated()
                
                // GET feedback peut être public
                .requestMatchers(HttpMethod.GET, "/api/feedback").permitAll()
                
                // Modération réservée aux rôles spécifiques
                .requestMatchers(HttpMethod.POST, "/api/feedback/moderate/**")
                    .hasAnyRole("ADMIN", "MODERATOR")
                
                // Analyse réservée aux rôles spécifiques
                .requestMatchers("/api/analysis/**")
                    .hasAnyRole("ADMIN", "MANAGER")
                .anyRequest().authenticated()
            )
            .sessionManagement(sess -> sess
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(ex -> ex
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
                })
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                }));

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",
            "http://127.0.0.1:3000",
            "https://your-production-domain.com"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "X-Requested-With",
            "Accept",
            "Origin",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        ));
        configuration.setExposedHeaders(Arrays.asList(
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials"
        ));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}