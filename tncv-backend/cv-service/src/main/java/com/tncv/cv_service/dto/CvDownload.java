package com.tncv.cv_service.dto;

import java.io.InputStream;

/**
 * Holder for downloaded CV file metadata and stream.
 */
public record CvDownload(
        InputStream inputStream,
        String fileName,
        String contentType,
        long contentLength
) {
}
