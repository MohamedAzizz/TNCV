package com.tncv.user_service.service;

import com.tncv.user_service.dto.RegisterRequest;
import com.tncv.user_service.entity.User;
import com.tncv.user_service.repository.UserRepository;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import org.springframework.stereotype.Service;

import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final Keycloak keycloak;

    public UserService(
            UserRepository userRepository,
            Keycloak keycloak) {

        this.userRepository = userRepository;
        this.keycloak = keycloak;
    }

    // ============================================================
    // REGISTER
    // ============================================================

    public User register(RegisterRequest request) {

        // --------------------------------------------------------
        // 1. Vérifier email
        // --------------------------------------------------------

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {

            throw new RuntimeException(
                    "Email déjà utilisé"
            );
        }

        // --------------------------------------------------------
        // 2. Vérifier username dans Keycloak
        // --------------------------------------------------------

        UsersResource usersResource =
                keycloak.realm("tncv").users();

        if (!usersResource
                .searchByUsername(request.getUsername(), true)
                .isEmpty()) {

            throw new RuntimeException(
                    "Username déjà utilisé"
            );
        }

        // --------------------------------------------------------
        // 3. Créer User Keycloak
        // --------------------------------------------------------

        UserRepresentation keycloakUser =
                new UserRepresentation();

        keycloakUser.setUsername(
                request.getUsername()
        );

        keycloakUser.setEmail(
                request.getEmail()
        );

        keycloakUser.setFirstName(
                request.getFirstName()
        );

        keycloakUser.setLastName(
                request.getLastName()
        );

        keycloakUser.setEnabled(true);

        keycloakUser.setEmailVerified(false);

        // --------------------------------------------------------
        // 4. Password
        // --------------------------------------------------------

        CredentialRepresentation password =
                new CredentialRepresentation();

        password.setType(
                CredentialRepresentation.PASSWORD
        );

        password.setValue(
                request.getPassword()
        );

        password.setTemporary(false);

        keycloakUser.setCredentials(
                Collections.singletonList(password)
        );

        // --------------------------------------------------------
        // 5. Envoyer à Keycloak
        // --------------------------------------------------------

        Response response =
                usersResource.create(keycloakUser);

        int status = response.getStatus();

        // --------------------------------------------------------
        // 6. Vérifier réponse Keycloak
        // --------------------------------------------------------

        if (status != 201) {

            String error = "";

            try {
                if (response.hasEntity()) {
                    error = response.readEntity(String.class);
                }
            } finally {
                response.close();
            }

            throw new RuntimeException(
                    "Erreur Keycloak : "
                            + status
                            + " - "
                            + error
            );
        }

        // --------------------------------------------------------
        // 7. Récupérer Keycloak ID
        // --------------------------------------------------------

        String location =
                response.getHeaderString("Location");

        response.close();

        if (location == null || location.isBlank()) {

            throw new RuntimeException(
                    "Keycloak ID introuvable"
            );
        }

        String keycloakId =
                location.substring(
                        location.lastIndexOf("/") + 1
                );

        // --------------------------------------------------------
        // 8. Créer User PostgreSQL
        // --------------------------------------------------------

        User user = new User();

        user.setKeycloakId(keycloakId);

        user.setFirstName(
                request.getFirstName()
        );

        user.setLastName(
                request.getLastName()
        );

        user.setEmail(
                request.getEmail()
        );

        user.setPhone(
                request.getPhone()
        );

        // --------------------------------------------------------
        // Date of birth
        // --------------------------------------------------------

        if (request.getDateOfBirth() != null
                && !request.getDateOfBirth().isBlank()) {

            user.setDateOfBirth(
                    LocalDate.parse(
                            request.getDateOfBirth()
                    )
            );
        }

        user.setAddress(
                request.getAddress()
        );

        user.setCountry(
                request.getCountry()
        );

        // --------------------------------------------------------
        // 9. Save PostgreSQL
        // --------------------------------------------------------

        return userRepository.save(user);
    }

    // ============================================================
    // GET ALL USERS
    // ============================================================

    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    // ============================================================
    // GET USER BY ID
    // ============================================================

    public User getUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Utilisateur introuvable avec l'id : "
                                        + id
                        )
                );
    }

    // ============================================================
    // GET USER BY KEYCLOAK ID
    // ============================================================

    public User getUserByKeycloakId(
            String keycloakId) {

        return userRepository
                .findByKeycloakId(keycloakId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Utilisateur Keycloak introuvable"
                        )
                );
    }

    // ============================================================
    // UPDATE USER
    // ============================================================

    public User updateUser(
            Long id,
            User updatedUser) {

        User existingUser =
                getUserById(id);

        existingUser.setFirstName(
                updatedUser.getFirstName()
        );

        existingUser.setLastName(
                updatedUser.getLastName()
        );

        existingUser.setEmail(
                updatedUser.getEmail()
        );

        existingUser.setPhone(
                updatedUser.getPhone()
        );

        existingUser.setDateOfBirth(
                updatedUser.getDateOfBirth()
        );

        existingUser.setProfilePhoto(
                updatedUser.getProfilePhoto()
        );

        existingUser.setAddress(
                updatedUser.getAddress()
        );

        existingUser.setCountry(
                updatedUser.getCountry()
        );

        return userRepository.save(
                existingUser
        );
    }

    // ============================================================
    // DELETE USER
    // ============================================================

    public void deleteUser(Long id) {

        User user =
                getUserById(id);

        userRepository.delete(user);
    }
}