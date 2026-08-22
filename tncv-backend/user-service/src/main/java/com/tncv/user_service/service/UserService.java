package com.tncv.user_service.service;

import com.tncv.user_service.dto.UserRequest;
import com.tncv.user_service.dto.UserResponse;
import com.tncv.user_service.entity.UserProfile;
import com.tncv.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // ============================================================
    // GET CURRENT USER
    // ============================================================

    public UserResponse getUserByKeycloakUserId(
            String keycloakUserId) {

        UserProfile user =
                userRepository
                        .findByKeycloakUserId(
                                keycloakUserId
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Profil utilisateur introuvable"
                                )
                        );

        return mapToResponse(user);
    }

    // ============================================================
    // GET USER BY ID
    // ============================================================

    public UserResponse getUserById(
            UUID id) {

        UserProfile user =
                userRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Utilisateur introuvable"
                                )
                        );

        return mapToResponse(user);
    }

    // ============================================================
    // GET ALL USERS
    // ============================================================

    public List<UserResponse> getAllUsers() {

        return userRepository
                .findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ============================================================
    // UPDATE CURRENT USER
    // ============================================================

    public UserResponse updateCurrentUser(
            String keycloakUserId,
            UserRequest request) {

        UserProfile user =
                userRepository
                        .findByKeycloakUserId(
                                keycloakUserId
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Profil utilisateur introuvable"
                                )
                        );

        user.setFirstName(
                request.getFirstName()
        );

        user.setLastName(
                request.getLastName()
        );

        user.setPhone(
                request.getPhone()
        );

        user.setProfileImage(
                request.getProfileImage()
        );

        user.setProfession(
                request.getProfession()
        );

        UserProfile updatedUser =
                userRepository.save(user);

        return mapToResponse(updatedUser);
    }

    // ============================================================
    // DELETE USER
    // ============================================================

    public void deleteUser(UUID id) {

        UserProfile user =
                userRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Utilisateur introuvable"
                                )
                        );

        userRepository.delete(user);
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