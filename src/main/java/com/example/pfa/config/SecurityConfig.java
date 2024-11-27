package com.example.pfa.config;

import com.example.pfa.security.JwtService;
import com.example.pfa.repository.UserRepo;
import com.example.pfa.config.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtService jwtService;
    private final UserRepo userRepo;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Désactive CSRF car on utilise JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Pas de session serveur avec JWT
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/auth/signin", "/auth/register").permitAll() // Pages publiques
                        .requestMatchers("/auth/**").permitAll() // Routes publiques pour l'authentification
                        .requestMatchers("/documents/**", "/demandes/**").authenticated() // Routes protégées
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Routes pour les administrateurs
                        .anyRequest().authenticated() // Toute autre requête nécessite une authentification
                )
                .formLogin(form -> form
                        .loginPage("/auth/signin").permitAll())

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // Configuration CORS avec la nouvelle approche Spring Security
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configuration CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:4200"); // Ajouter l'URL du frontend
        configuration.addAllowedMethod("*"); // Autoriser toutes les méthodes
        configuration.addAllowedHeader("*"); // Autoriser tous les en-têtes
        configuration.setAllowCredentials(true); // Autoriser les cookies et autres informations d'authentification

        // Appliquer la configuration CORS pour toutes les routes
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Applique pour toutes les routes
        return source;
    }
}
