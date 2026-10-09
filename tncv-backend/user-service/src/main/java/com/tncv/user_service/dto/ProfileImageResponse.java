package com.tncv.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for profile image operations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileImageResponse {

    private String message;

    /**
     * Presigned URL valid for a limited time to read the image.
     */
    private String imageUrl;
}
