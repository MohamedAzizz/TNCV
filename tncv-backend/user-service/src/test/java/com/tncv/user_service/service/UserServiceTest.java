package com.tncv.user_service.service;

import com.tncv.user_service.dto.ProfileImageResponse;
import com.tncv.user_service.entity.UserProfile;
import com.tncv.user_service.exception.FileTooLargeException;
import com.tncv.user_service.exception.InvalidFileTypeException;
import com.tncv.user_service.exception.ResourceNotFoundException;
import com.tncv.user_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MinioService minioService;

    @InjectMocks
    private UserService userService;

    private UserProfile mockUser;
    private final String KEYCLOAK_ID = "kc-user-123";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userService, "profileImagesBucket", "tncv-profile-images");
        ReflectionTestUtils.setField(userService, "maxImageSizeBytes", 5242880L);

        mockUser = UserProfile.builder()
                .id(UUID.randomUUID())
                .keycloakUserId(KEYCLOAK_ID)
                .username("testuser")
                .firstName("Test")
                .lastName("User")
                .email("test@example.com")
                .build();
    }

    @Test
    void uploadProfileImage_success() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "profile.jpg",
                "image/jpeg",
                new byte[]{1, 2, 3, 4}
        );

        when(userRepository.findByKeycloakUserId(KEYCLOAK_ID)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(UserProfile.class))).thenReturn(mockUser);
        when(minioService.generatePresignedUrl(eq("tncv-profile-images"), anyString(), eq(3600)))
                .thenReturn("http://minio/signed-url");

        ProfileImageResponse response = userService.uploadProfileImage(KEYCLOAK_ID, file);

        assertNotNull(response);
        assertEquals("http://minio/signed-url", response.getImageUrl());
        verify(minioService, times(1)).uploadFile(
                eq("tncv-profile-images"),
                anyString(),
                any(InputStream.class),
                eq("image/jpeg"),
                eq(4L)
        );
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    void uploadProfileImage_replacesOldImage() {
        mockUser.setProfileImage("users/" + KEYCLOAK_ID + "/old-image.jpg");

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "new-profile.png",
                "image/png",
                new byte[]{1, 2, 3}
        );

        when(userRepository.findByKeycloakUserId(KEYCLOAK_ID)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(UserProfile.class))).thenReturn(mockUser);
        when(minioService.generatePresignedUrl(eq("tncv-profile-images"), anyString(), eq(3600)))
                .thenReturn("http://minio/new-url");

        ProfileImageResponse response = userService.uploadProfileImage(KEYCLOAK_ID, file);

        assertNotNull(response);
        verify(minioService, times(1)).deleteFile(
                eq("tncv-profile-images"),
                eq("users/" + KEYCLOAK_ID + "/old-image.jpg")
        );
    }

    @Test
    void uploadProfileImage_invalidFileType_throwsException() {
        MockMultipartFile exeFile = new MockMultipartFile(
                "file",
                "malicious.exe",
                "application/x-msdownload",
                new byte[]{1, 2, 3}
        );

        assertThrows(InvalidFileTypeException.class, () ->
                userService.uploadProfileImage(KEYCLOAK_ID, exeFile)
        );

        verify(minioService, never()).uploadFile(anyString(), anyString(), any(), anyString(), anyLong());
    }

    @Test
    void uploadProfileImage_fileTooLarge_throwsException() {
        byte[] oversized = new byte[6 * 1024 * 1024]; // 6 MB > 5 MB limit
        MockMultipartFile largeFile = new MockMultipartFile(
                "file",
                "large.jpg",
                "image/jpeg",
                oversized
        );

        assertThrows(FileTooLargeException.class, () ->
                userService.uploadProfileImage(KEYCLOAK_ID, largeFile)
        );

        verify(minioService, never()).uploadFile(anyString(), anyString(), any(), anyString(), anyLong());
    }

    @Test
    void uploadProfileImage_userNotFound_throwsResourceNotFound() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "avatar.webp",
                "image/webp",
                new byte[]{1, 2}
        );

        when(userRepository.findByKeycloakUserId("unknown")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                userService.uploadProfileImage("unknown", file)
        );
    }

    @Test
    void getProfileImageUrl_success() {
        mockUser.setProfileImage("users/" + KEYCLOAK_ID + "/image.jpg");
        when(userRepository.findByKeycloakUserId(KEYCLOAK_ID)).thenReturn(Optional.of(mockUser));
        when(minioService.generatePresignedUrl("tncv-profile-images", "users/" + KEYCLOAK_ID + "/image.jpg", 3600))
                .thenReturn("http://minio/signed-url");

        ProfileImageResponse response = userService.getProfileImageUrl(KEYCLOAK_ID);

        assertNotNull(response);
        assertEquals("http://minio/signed-url", response.getImageUrl());
    }

    @Test
    void getProfileImageUrl_noImageDefined() {
        mockUser.setProfileImage(null);
        when(userRepository.findByKeycloakUserId(KEYCLOAK_ID)).thenReturn(Optional.of(mockUser));

        ProfileImageResponse response = userService.getProfileImageUrl(KEYCLOAK_ID);

        assertNotNull(response);
        assertNull(response.getImageUrl());
    }

    @Test
    void deleteProfileImage_success() {
        mockUser.setProfileImage("users/" + KEYCLOAK_ID + "/avatar.jpg");
        when(userRepository.findByKeycloakUserId(KEYCLOAK_ID)).thenReturn(Optional.of(mockUser));

        userService.deleteProfileImage(KEYCLOAK_ID);

        verify(minioService, times(1)).deleteFile(
                eq("tncv-profile-images"),
                eq("users/" + KEYCLOAK_ID + "/avatar.jpg")
        );
        assertNull(mockUser.getProfileImage());
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    void deleteProfileImage_whenNoImagePresent_doesNothing() {
        mockUser.setProfileImage(null);
        when(userRepository.findByKeycloakUserId(KEYCLOAK_ID)).thenReturn(Optional.of(mockUser));

        userService.deleteProfileImage(KEYCLOAK_ID);

        verify(minioService, never()).deleteFile(anyString(), anyString());
        verify(userRepository, never()).save(any());
    }
}
