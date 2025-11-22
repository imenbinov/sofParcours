package com.hackathon.sofParcours.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Désactiver CSRF pour les API REST
            .csrf().disable()
            
            // Configuration des autorisations
            .authorizeRequests()
                // Endpoints publics (pas d'authentification requise)
                .antMatchers(
                    "/",
                    "/api/auth/**",           // Register & Login
                    "/api/rooms/**",          // Rooms
                    "/api/quiz/**",           // Quiz
                    "/api/badges/**",         // Badges
                    "/api/profile/**",        // Profils
                    "/api/leaderboard/**",    // Classements
                    "/api/feedback/**",       // Feedback
                    "/api/ai/**",             // IA
                    "/swagger-ui/**",         // Swagger UI
                    "/swagger-ui.html",       // Swagger UI
                    "/v3/api-docs/**",        // OpenAPI docs
                    "/actuator/**"            // Actuator (si activé)
                ).permitAll()
                
                // Tous les autres endpoints nécessitent une authentification
                .anyRequest().authenticated()
            
            .and()
            
            // Configuration de session pour API REST (stateless)
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            
            .and()
            
            // Désactiver la page de login par défaut
            .formLogin().disable()
            .httpBasic().disable();

        return http.build();
    }
}
