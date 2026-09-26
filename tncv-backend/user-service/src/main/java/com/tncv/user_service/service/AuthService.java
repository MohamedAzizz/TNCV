package com.tncv.user_service.service;

import com.tncv.user_service.dto.LoginRequest;
import com.tncv.user_service.dto.LoginResponse;
import com.tncv.user_service.dto.RegisterRequest;
import com.tncv.user_service.dto.UserResponse;
import com.tncv.user_service.entity.UserProfile;
import com.tncv.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final KeycloakService keycloakService;


    // ============================================================
    // REGISTER
    // ============================================================

    public UserResponse register(
            RegisterRequest request) {

        // Vérifier EMAIL
        if (userRepository.existsByEmail(
                request.getEmail())) {

            throw new RuntimeException(
                    "Un compte avec cet email existe déjà"
            );
        }


        // Vérifier USERNAME
        if (userRepository.existsByUsername(
                request.getUsername())) {

            throw new RuntimeException(
                    "Ce username est déjà utilisé"
            );
        }


        String keycloakUserId = null;

        try {

            // Créer utilisateur Keycloak
            keycloakUserId =
                    keycloakService.createUser(
                            request.getUsername(),
                            request.getFirstName(),
                            request.getLastName(),
                            request.getEmail(),
                            request.getPassword()
                    );


            // Créer profil PostgreSQL
            UserProfile user =
                    UserProfile.builder()
                            .keycloakUserId(
                                    keycloakUserId
                            )
                            .username(
                                    request.getUsername()
                            )
                            .firstName(
                                    request.getFirstName()
                            )
                            .lastName(
                                    request.getLastName()
                            )
                            .email(
                                    request.getEmail()
                            )
                            .phone(
                                    request.getPhone()
                            )
                            .profileImage(
                                    request.getProfileImage()
                            )
                            .profession(
                                    request.getProfession()
                            )
                            .build();


            UserProfile savedUser =
                    userRepository.save(user);


            return mapToResponse(savedUser);

        } catch (Exception exception) {

            // Rollback Keycloak
            if (keycloakUserId != null) {

                try {

                    keycloakService.deleteUser(
                            keycloakUserId
                    );

                } catch (Exception ignored) {
                }
            }

            throw new RuntimeException(
                    "Erreur lors de l'inscription : "
                            + exception.getMessage(),
                    exception
            );
        }
    }


    // ============================================================
    // LOGIN
    // ============================================================

    /**
     * Le frontend envoie un email + mot de passe.
     * On résout l'email en username (stocké en BDD) avant d'appeler Keycloak,
     * car Keycloak authentifie par username dans ce realm.
     */
    public LoginResponse login(
            LoginRequest request) {

        // Résoudre le username à partir de l'email
        UserProfile user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Identifiants incorrects"
                ));

        return keycloakService.login(
                user.getUsername(),
                request.getPassword()
        );
    }


    // ============================================================
    // MAPPING
    // ============================================================

    private UserResponse mapToResponse(
            UserProfile user) {

        return UserResponse.builder()
                .id(user.getId())
                .keycloakUserId(
                        user.getKeycloakUserId()
                )
                .username(
                        user.getUsername()
                )
                .firstName(
                        user.getFirstName()
                )
                .lastName(
                        user.getLastName()
                )
                .email(
                        user.getEmail()
                )
                .phone(
                        user.getPhone()
                )
                .profileImage(
                        user.getProfileImage()
                )
                .profession(
                        user.getProfession()
                )
                .createdAt(
                        user.getCreatedAt()
                )
                .updatedAt(
                        user.getUpdatedAt()
                )
                .build();
    }
}