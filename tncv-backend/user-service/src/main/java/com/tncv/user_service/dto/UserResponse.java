package com.tncv.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private UUID id;

    private String keycloakUserId;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String profileImage;

    private String profession;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}