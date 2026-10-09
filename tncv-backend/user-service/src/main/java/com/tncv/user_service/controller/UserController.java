package com.tncv.user_service.controller;

import com.tncv.user_service.dto.ProfileImageResponse;
import com.tncv.user_service.dto.UserRequest;
import com.tncv.user_service.dto.UserResponse;
import com.tncv.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {

        String keycloakUserId = authentication.getName();

        return ResponseEntity.ok(
                userService.getUserByKeycloakUserId(keycloakUserId)
        );
    }

    // ============================================================
    // UPDATE CURRENT USER
    // ============================================================

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateCurrentUser(
            Authentication authentication,
            @Valid @RequestBody UserRequest request) {

        String keycloakUserId = authentication.getName();

        return ResponseEntity.ok(
                userService.updateCurrentUser(keycloakUserId, request)
        );
    }

    // ============================================================
    // UPLOAD PROFILE IMAGE
    // PUT /api/users/me/profile-image
    // ============================================================

    @RequestMapping(value = "/me/profile-image", method = {RequestMethod.PUT, RequestMethod.POST}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProfileImageResponse> uploadProfileImage(
            Authentication authentication,
            @RequestParam("file") MultipartFile file) {

        String keycloakUserId = authentication.getName();

        return ResponseEntity.ok(
                userService.uploadProfileImage(keycloakUserId, file)
        );
    }

    // ============================================================
    // GET PROFILE IMAGE (presigned URL)
    // GET /api/users/me/profile-image
    // ============================================================

    @GetMapping("/me/profile-image")
    public ResponseEntity<ProfileImageResponse> getProfileImageUrl(Authentication authentication) {

        String keycloakUserId = authentication.getName();

        return ResponseEntity.ok(
                userService.getProfileImageUrl(keycloakUserId)
        );
    }

    // ============================================================
    // DELETE PROFILE IMAGE
    // DELETE /api/users/me/profile-image
    // ============================================================

    @DeleteMapping("/me/profile-image")
    public ResponseEntity<Void> deleteProfileImage(Authentication authentication) {

        String keycloakUserId = authentication.getName();
        userService.deleteProfileImage(keycloakUserId);

        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // ADMIN - GET USER
    // ============================================================

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {

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
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}