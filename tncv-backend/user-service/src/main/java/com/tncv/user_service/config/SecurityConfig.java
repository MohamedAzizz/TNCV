package com.tncv.user_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            // ====================================================
            // CSRF
            // ====================================================
            .csrf(csrf -> csrf.disable())

            // ====================================================
            // SESSION
            // ====================================================
            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            // ====================================================
            // AUTHORIZATION
            // ====================================================
            .authorizeHttpRequests(auth -> auth

                // التسجيل بدون Access Token
                .requestMatchers("/users/register")
                .permitAll()

                // كل شيء آخر يحتاج JWT
                .anyRequest()
                .authenticated()
            )

            // ====================================================
            // KEYCLOAK JWT
            // ====================================================
            .oauth2ResourceServer(oauth2 ->
                oauth2.jwt(Customizer.withDefaults())
            );

        return http.build();
    }
}