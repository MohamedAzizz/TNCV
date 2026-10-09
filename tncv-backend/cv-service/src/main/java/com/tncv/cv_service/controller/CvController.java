package com.tncv.cv_service.controller;

import com.tncv.cv_service.dto.CvDownload;
import com.tncv.cv_service.dto.CvFileResponse;
import com.tncv.cv_service.dto.CvRequest;
import com.tncv.cv_service.dto.CvResponse;
import com.tncv.cv_service.service.CvService;
import jakarta.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/cvs")
public class CvController {

    private final CvService cvService;

    public CvController(CvService cvService) {
        this.cvService = cvService;
    }

    // ============================================================
    // CREATE CV (Metadata)
    // POST /api/cvs
    // ============================================================

    @PostMapping
    public ResponseEntity<CvResponse> createCv(
            Authentication authentication,
            @Valid @RequestBody CvRequest request) {

        String userId = authentication.getName();

        CvResponse response = cvService.createCv(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // ============================================================
    // GET ALL MY CVS
    // GET /api/cvs
    // ============================================================

    @GetMapping
    public ResponseEntity<List<CvResponse>> getUserCvs(
            Authentication authentication) {

        String userId = authentication.getName();

        return ResponseEntity.ok(
                cvService.getUserCvs(userId));
    }

    // ============================================================
    // GET ONE CV
    // GET /api/cvs/{id}
    // ============================================================

    @GetMapping("/{id}")
    public ResponseEntity<CvResponse> getCv(
            @PathVariable Long id,
            Authentication authentication) {

        String userId = authentication.getName();

        return ResponseEntity.ok(
                cvService.getCv(id, userId));
    }

    // ============================================================
    // UPDATE MY CV
    // PUT /api/cvs/{id}
    // ============================================================

    @PutMapping("/{id}")
    public ResponseEntity<CvResponse> updateCv(
            @PathVariable Long id,
            Authentication authentication,
            @Valid @RequestBody CvRequest request) {

        String userId = authentication.getName();

        return ResponseEntity.ok(
                cvService.updateCv(id, userId, request));
    }

    // ============================================================
    // DELETE MY CV
    // DELETE /api/cvs/{id}
    // ============================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCv(
            @PathVariable Long id,
            Authentication authentication) {

        String userId = authentication.getName();

        cvService.deleteCv(id, userId);

        return ResponseEntity
                .noContent()
                .build();
    }

    // ============================================================
    // UPLOAD CV FILE (PDF / DOCX)
    // POST /api/cvs/upload
    // ============================================================

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CvFileResponse> uploadCvFile(
            Authentication authentication,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "cvId", required = false) Long cvId) {

        String userId = authentication.getName();
        CvFileResponse response = cvService.uploadCvFile(userId, cvId, title, file);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // ============================================================
    // ATTACH / REPLACE FILE ON EXISTING CV
    // POST /api/cvs/{id}/file
    // ============================================================

    @PostMapping(value = "/{id}/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CvFileResponse> uploadCvFileForExisting(
            @PathVariable Long id,
            Authentication authentication,
            @RequestParam("file") MultipartFile file) {

        String userId = authentication.getName();
        CvFileResponse response = cvService.uploadCvFile(userId, id, null, file);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // ============================================================
    // DOWNLOAD CV FILE
    // GET /api/cvs/{id}/file
    // ============================================================

    @GetMapping("/{id}/file")
    public ResponseEntity<Resource> downloadCvFile(
            @PathVariable Long id,
            Authentication authentication) {

        String userId = authentication.getName();
        CvDownload download = cvService.downloadCvFile(id, userId);

        InputStreamResource resource = new InputStreamResource(download.inputStream());

        ContentDisposition contentDisposition = ContentDisposition.attachment()
                .filename(download.fileName(), StandardCharsets.UTF_8)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(contentDisposition);
        if (download.contentLength() > 0) {
            headers.setContentLength(download.contentLength());
        }

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(download.contentType()))
                .body(resource);
    }

    // ============================================================
    // DELETE CV FILE
    // DELETE /api/cvs/{id}/file
    // ============================================================

    @DeleteMapping("/{id}/file")
    public ResponseEntity<Void> deleteCvFile(
            @PathVariable Long id,
            Authentication authentication) {

        String userId = authentication.getName();
        cvService.deleteCvFile(id, userId);

        return ResponseEntity
                .noContent()
                .build();
    }
}