package com.tncv.user_service.controller;

import com.tncv.user_service.dto.UserRequest;
import com.tncv.user_service.dto.UserResponse;
import com.tncv.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ============================================================
    // CURRENT USER
    // ============================================================

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(
            Authentication authentication) {

        String keycloakUserId =
                authentication.getName();

        return ResponseEntity.ok(
                userService.getUserByKeycloakUserId(
                        keycloakUserId
                )
        );
    }

    // ============================================================
    // UPDATE CURRENT USER
    // ============================================================

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateCurrentUser(
            Authentication authentication,
            @Valid @RequestBody UserRequest request) {

        String keycloakUserId =
                authentication.getName();

        return ResponseEntity.ok(
                userService.updateCurrentUser(
                        keycloakUserId,
                        request
                )
        );
    }

    // ============================================================
    // ADMIN - GET USER
    // ============================================================

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                userService.getUserById(id)
        );
    }

    // ============================================================
    // ADMIN - GET ALL USERS
    // ============================================================

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        return ResponseEntity.ok(
                userService.getAllUsers()
        );
    }

    // ============================================================
    // ADMIN - DELETE USER
    // ============================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable UUID id) {

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}