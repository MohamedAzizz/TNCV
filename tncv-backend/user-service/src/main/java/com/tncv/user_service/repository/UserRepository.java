package com.tncv.user_service.repository;

import com.tncv.user_service.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserProfile, UUID> {

    Optional<UserProfile> findByEmail(String email);

    Optional<UserProfile> findByKeycloakUserId(String keycloakUserId);

    boolean existsByEmail(String email);

    boolean existsByKeycloakUserId(String keycloakUserId);
}