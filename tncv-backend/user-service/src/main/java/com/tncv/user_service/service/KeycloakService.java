package com.tncv.user_service.service;

import com.tncv.user_service.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    private final Keycloak keycloak;

    private final RestClient.Builder restClientBuilder;


    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;


    // ============================================================
    // CREATE USER
    // ============================================================

    public String createUser(
            String username,
            String firstName,
            String lastName,
            String email,
            String password) {

        UserRepresentation user =
                new UserRepresentation();

        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);

        user.setEnabled(true);
        user.setEmailVerified(false);


        // ========================================================
        // PASSWORD
        // ========================================================

        CredentialRepresentation credential =
                new CredentialRepresentation();

        credential.setType(
                CredentialRepresentation.PASSWORD
        );

        credential.setValue(password);
        credential.setTemporary(false);

        user.setCredentials(
                List.of(credential)
        );


        // ========================================================
        // CREATE USER
        // ========================================================

        Response response =
                keycloak
                        .realm(realm)
                        .users()
                        .create(user);

        try {

            if (response.getStatus() != 201) {

                throw new RuntimeException(
                        "Erreur lors de la création du compte Keycloak. HTTP "
                                + response.getStatus()
                );
            }

            String location =
                    response.getHeaderString("Location");

            if (location == null) {

                throw new RuntimeException(
                        "Keycloak n'a pas retourné l'ID utilisateur"
                );
            }

            String keycloakUserId =
                    location.substring(
                            location.lastIndexOf("/") + 1
                    );


            // ====================================================
            // ROLE USER
            // ====================================================

            assignUserRole(keycloakUserId);


            return keycloakUserId;

        } finally {

            response.close();
        }
    }


    // ============================================================
    // LOGIN
    // ============================================================

    public LoginResponse login(
            String username,
            String password) {

        String tokenUrl =
                serverUrl
                        + "/realms/"
                        + realm
                        + "/protocol/openid-connect/token";


        MultiValueMap<String, String> form =
                new LinkedMultiValueMap<>();

        form.add(
                "grant_type",
                OAuth2Constants.PASSWORD
        );

        form.add(
                "client_id",
                clientId
        );

        form.add(
                "client_secret",
                clientSecret
        );

        form.add(
                "username",
                username
        );

        form.add(
                "password",
                password
        );


        Map<String, Object> response =
                restClientBuilder
                        .build()
                        .post()
                        .uri(tokenUrl)
                        .contentType(
                                MediaType.APPLICATION_FORM_URLENCODED
                        )
                        .body(form)
                        .retrieve()
                        .body(Map.class);


        if (response == null) {

            throw new RuntimeException(
                    "Keycloak n'a pas retourné de token"
            );
        }


        return LoginResponse.builder()

                .accessToken(
                        (String) response.get(
                                "access_token"
                        )
                )

                .refreshToken(
                        (String) response.get(
                                "refresh_token"
                        )
                )

                .tokenType(
                        (String) response.get(
                                "token_type"
                        )
                )

                .expiresIn(
                        getLongValue(
                                response.get(
                                        "expires_in"
                                )
                        )
                )

                .refreshExpiresIn(
                        getLongValue(
                                response.get(
                                        "refresh_expires_in"
                                )
                        )
                )

                .build();
    }


    // ============================================================
    // ROLE USER
    // ============================================================

    private void assignUserRole(
            String keycloakUserId) {

        RoleRepresentation userRole =
                keycloak
                        .realm(realm)
                        .roles()
                        .get("USER")
                        .toRepresentation();


        keycloak
                .realm(realm)
                .users()
                .get(keycloakUserId)
                .roles()
                .realmLevel()
                .add(List.of(userRole));
    }


    // ============================================================
    // DELETE USER
    // ============================================================

    public void deleteUser(
            String keycloakUserId) {

        keycloak
                .realm(realm)
                .users()
                .delete(keycloakUserId);
    }


    // ============================================================
    // LONG CONVERSION
    // ============================================================

    private Long getLongValue(
            Object value) {

        if (value == null) {
            return null;
        }

        if (value instanceof Number number) {
            return number.longValue();
        }

        return Long.parseLong(
                value.toString()
        );
    }
}