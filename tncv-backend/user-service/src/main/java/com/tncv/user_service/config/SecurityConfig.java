package com.tncv.user_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

                // =================================================
                // CSRF
                // =================================================

                .csrf(AbstractHttpConfigurer::disable)


                // =================================================
                // AUTHORIZATION
                // =================================================

                .authorizeHttpRequests(auth -> auth

                        // -----------------------------------------
                        // REGISTER
                        // -----------------------------------------

                        .requestMatchers(
                                "/api/auth/register"
                        ).permitAll()


                        // -----------------------------------------
                        // LOGIN
                        // -----------------------------------------

                        .requestMatchers(
                                "/api/auth/login"
                        ).permitAll()


                        // -----------------------------------------
                        // CURRENT USER
                        // -----------------------------------------

                        .requestMatchers(
                                "/api/users/me"
                        ).hasAnyRole(
                                "USER",
                                "ADMIN"
                        )


                        // -----------------------------------------
                        // ADMIN
                        // -----------------------------------------

                        .requestMatchers(
                                "/api/users",
                                "/api/users/**"
                        ).hasRole("ADMIN")


                        // -----------------------------------------
                        // OTHER
                        // -----------------------------------------

                        .anyRequest()
                        .authenticated()
                )


                // =================================================
                // JWT
                // =================================================

                .oauth2ResourceServer(
                        oauth2 -> oauth2.jwt(
                                jwt -> jwt.jwtAuthenticationConverter(
                                        new KeycloakJwtAuthenticationConverter()
                                )
                        )
                );

        return http.build();
    }
}