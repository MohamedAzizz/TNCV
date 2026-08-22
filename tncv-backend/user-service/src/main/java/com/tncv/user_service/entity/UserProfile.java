package com.tncv.user_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "user_profiles",
        uniqueConstraints = {

                @UniqueConstraint(
                        name = "uk_user_keycloak_id",
                        columnNames = "keycloak_user_id"
                ),

                @UniqueConstraint(
                        name = "uk_user_username",
                        columnNames = "username"
                ),

                @UniqueConstraint(
                        name = "uk_user_email",
                        columnNames = "email"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // ============================================================
    // KEYCLOAK ID
    // ============================================================

    @Column(
            name = "keycloak_user_id",
            nullable = false,
            unique = true
    )
    private String keycloakUserId;

    // ============================================================
    // USERNAME
    // ============================================================

    @Column(
            nullable = false,
            unique = true
    )
    private String username;

    // ============================================================
    // PERSONAL INFORMATION
    // ============================================================

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    private String profileImage;

    private String profession;

    // ============================================================
    // DATES
    // ============================================================

    @Column(
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // ============================================================
    // CREATE
    // ============================================================

    @PrePersist
    protected void onCreate() {

        LocalDateTime now =
                LocalDateTime.now();

        createdAt = now;
        updatedAt = now;
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @PreUpdate
    protected void onUpdate() {

        updatedAt =
                LocalDateTime.now();
    }
}