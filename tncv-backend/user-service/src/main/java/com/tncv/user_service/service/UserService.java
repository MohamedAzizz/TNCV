package com.tncv.user_service.service;

import com.tncv.user_service.dto.ProfileImageResponse;
import com.tncv.user_service.dto.UserRequest;
import com.tncv.user_service.dto.UserResponse;
import com.tncv.user_service.entity.UserProfile;
import com.tncv.user_service.exception.FileTooLargeException;
import com.tncv.user_service.exception.InvalidFileTypeException;
import com.tncv.user_service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.tncv.user_service.repository.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MinioService minioService;

    @Value("${minio.bucket.profile-images}")
    private String profileImagesBucket;

    @Value("${tncv.profile-image.max-size-bytes:5242880}")
    private long maxImageSizeBytes;

    /**
     * Allowed MIME types for profile images.
     */
    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    /**
     * Presigned URL expiry in seconds (1 hour).
     */
    private static final int PRESIGNED_URL_EXPIRY = 3600;

    // ============================================================
    // GET CURRENT USER
    // ============================================================

    public UserResponse getUserByKeycloakUserId(String keycloakUserId) {

        UserProfile user = userRepository
                .findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Profil utilisateur introuvable pour l'ID Keycloak: " + keycloakUserId
                ));

        return mapToResponse(user);
    }

    // ============================================================
    // GET USER BY ID
    // ============================================================

    public UserResponse getUserById(UUID id) {

        UserProfile user = userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Utilisateur introuvable avec l'id: " + id
                ));

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

    @Transactional
    public UserResponse updateCurrentUser(String keycloakUserId, UserRequest request) {

        UserProfile user = userRepository
                .findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Profil utilisateur introuvable"
                ));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setProfession(request.getProfession());
        // Note: profileImage is managed separately via upload/delete endpoints
        // We do NOT update it through UserRequest to avoid overwriting MinIO reference

        return mapToResponse(userRepository.save(user));
    }

    // ============================================================
    // DELETE USER
    // ============================================================

    @Transactional
    public void deleteUser(UUID id) {

        UserProfile user = userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Utilisateur introuvable avec l'id: " + id
                ));

        // Also clean up profile image in MinIO if present
        if (user.getProfileImage() != null) {
            try {
                minioService.deleteFile(profileImagesBucket, user.getProfileImage());
            } catch (Exception e) {
                log.warn("Could not delete profile image from MinIO for user {}: {}", id, e.getMessage());
            }
        }

        userRepository.delete(user);
    }

    // ============================================================
    // UPLOAD PROFILE IMAGE
    // ============================================================

    /**
     * Uploads a profile image for the authenticated user.
     * Strategy:
     *   1. Validate file (type + size)
     *   2. Upload to MinIO
     *   3. Delete old image from MinIO (if any)
     *   4. Save MinIO object name in PostgreSQL
     *   5. Return presigned URL
     *
     * If the DB save fails, the new MinIO object is rolled back.
     */
    @Transactional
    public ProfileImageResponse uploadProfileImage(String keycloakUserId, MultipartFile file) {

        // Validate
        validateImageFile(file);

        UserProfile user = userRepository
                .findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Profil utilisateur introuvable"
                ));

        // Generate secure object name
        String extension = getExtension(file.getContentType());
        String objectName = "users/" + keycloakUserId + "/" + UUID.randomUUID() + extension;

        String newObjectName = null;
        try {
            // Upload new image to MinIO
            log.info("Uploading profile image for user: {}", keycloakUserId);
            minioService.uploadFile(
                    profileImagesBucket,
                    objectName,
                    file.getInputStream(),
                    file.getContentType(),
                    file.getSize()
            );
            newObjectName = objectName;

            // Delete old image from MinIO if present
            String oldObjectName = user.getProfileImage();
            if (oldObjectName != null) {
                try {
                    minioService.deleteFile(profileImagesBucket, oldObjectName);
                    log.info("Deleted old profile image from MinIO: {}", oldObjectName);
                } catch (Exception e) {
                    log.warn("Could not delete old profile image: {}", oldObjectName);
                }
            }

            // Save reference in DB
            user.setProfileImage(objectName);
            userRepository.save(user);

            // Generate presigned URL
            String presignedUrl = minioService.generatePresignedUrl(
                    profileImagesBucket, objectName, PRESIGNED_URL_EXPIRY);

            log.info("Profile image upload successful for user: {}", keycloakUserId);
            return ProfileImageResponse.builder()
                    .message("Photo de profil mise à jour avec succès")
                    .imageUrl(presignedUrl)
                    .build();

        } catch (IOException e) {
            // Rollback: delete just-uploaded MinIO object
            if (newObjectName != null) {
                rollbackMinioUpload(profileImagesBucket, newObjectName);
            }
            throw new com.tncv.user_service.exception.StorageException("Erreur lors de la lecture du fichier", e);
        } catch (com.tncv.user_service.exception.StorageException e) {
            // Rollback MinIO if DB save already happened but re-throw
            throw e;
        }
    }

    // ============================================================
    // DELETE PROFILE IMAGE
    // ============================================================

    /**
     * Deletes the profile image of the authenticated user.
     */
    @Transactional
    public void deleteProfileImage(String keycloakUserId) {

        UserProfile user = userRepository
                .findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Profil utilisateur introuvable"
                ));

        String objectName = user.getProfileImage();

        if (objectName == null) {
            log.info("No profile image to delete for user: {}", keycloakUserId);
            return;
        }

        // Delete from MinIO first
        log.info("Deleting profile image from MinIO for user: {}", keycloakUserId);
        minioService.deleteFile(profileImagesBucket, objectName);

        // Update DB
        user.setProfileImage(null);
        userRepository.save(user);

        log.info("Profile image deleted successfully for user: {}", keycloakUserId);
    }

    // ============================================================
    // GET PROFILE IMAGE URL (presigned)
    // ============================================================

    /**
     * Returns a presigned URL for the user's profile image (valid 1 hour).
     */
    public ProfileImageResponse getProfileImageUrl(String keycloakUserId) {

        UserProfile user = userRepository
                .findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Profil utilisateur introuvable"
                ));

        if (user.getProfileImage() == null) {
            return ProfileImageResponse.builder()
                    .message("Aucune photo de profil définie")
                    .imageUrl(null)
                    .build();
        }

        String presignedUrl = minioService.generatePresignedUrl(
                profileImagesBucket, user.getProfileImage(), PRESIGNED_URL_EXPIRY);

        return ProfileImageResponse.builder()
                .message("URL générée avec succès (valide 1 heure)")
                .imageUrl(presignedUrl)
                .build();
    }

    // ============================================================
    // PRIVATE HELPERS
    // ============================================================

    private void validateImageFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new InvalidFileTypeException("Le fichier est vide ou absent");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType)) {
            throw new InvalidFileTypeException(
                    "Type de fichier non autorisé. Types acceptés: image/jpeg, image/png, image/webp"
            );
        }

        if (file.getSize() > maxImageSizeBytes) {
            throw new FileTooLargeException(
                    "L'image dépasse la taille maximale autorisée (" + (maxImageSizeBytes / 1024 / 1024) + " MB)"
            );
        }
    }

    private String getExtension(String contentType) {
        return switch (contentType) {
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> ".jpg";
        };
    }

    private void rollbackMinioUpload(String bucket, String objectName) {
        try {
            minioService.deleteFile(bucket, objectName);
            log.info("Rollback: deleted MinIO object {}/{}", bucket, objectName);
        } catch (Exception ex) {
            log.error("Rollback failed: could not delete MinIO object {}/{}", bucket, objectName, ex);
        }
    }

    // ============================================================
    // MAPPING
    // ============================================================

    private UserResponse mapToResponse(UserProfile user) {

        return UserResponse.builder()
                .id(user.getId())
                .keycloakUserId(user.getKeycloakUserId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .profileImage(user.getProfileImage())
                .profession(user.getProfession())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}