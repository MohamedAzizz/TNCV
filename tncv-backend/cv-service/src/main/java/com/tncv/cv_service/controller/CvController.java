package com.tncv.cv_service.controller;

import com.tncv.cv_service.dto.CvRequest;
import com.tncv.cv_service.dto.CvResponse;
import com.tncv.cv_service.service.CvService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cvs")
public class CvController {

    private final CvService cvService;

    public CvController(CvService cvService) {
        this.cvService = cvService;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @PostMapping
    public ResponseEntity<CvResponse> createCv(
            Authentication authentication,
            @Valid @RequestBody CvRequest request) {

        String userId = authentication.getName();

        CvResponse response = cvService.createCv(
                userId,
                request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // ============================================================
    // GET ALL USER CVS
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
    // UPDATE
    // ============================================================

    @PutMapping("/{id}")
    public ResponseEntity<CvResponse> updateCv(
            @PathVariable Long id,
            Authentication authentication,
            @Valid @RequestBody CvRequest request) {

        String userId = authentication.getName();

        return ResponseEntity.ok(
                cvService.updateCv(
                        id,
                        userId,
                        request));
    }

    // ============================================================
    // DELETE
    // ============================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCv(
            @PathVariable Long id,
            Authentication authentication) {

        String userId = authentication.getName();

        cvService.deleteCv(
                id,
                userId);

        return ResponseEntity
                .noContent()
                .build();
    }
}