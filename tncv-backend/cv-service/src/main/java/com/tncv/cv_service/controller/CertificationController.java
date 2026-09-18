package com.tncv.cv_service.controller;

import com.tncv.cv_service.dto.CertificationRequest;
import com.tncv.cv_service.dto.CertificationResponse;
import com.tncv.cv_service.service.CertificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cvs/{cvId}/certifications")
public class CertificationController {

        private final CertificationService certificationService;

        public CertificationController(
                        CertificationService certificationService) {

                this.certificationService = certificationService;
        }

        // ============================================================
        // CREATE
        // ============================================================

        @PostMapping
        public ResponseEntity<CertificationResponse> create(
                        @PathVariable Long cvId,
                        Authentication authentication,
                        @Valid @RequestBody CertificationRequest request) {

                String userId = authentication.getName();

                CertificationResponse response = certificationService.create(
                                cvId,
                                userId,
                                request);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(response);
        }

        // ============================================================
        // GET ALL
        // ============================================================

        @GetMapping
        public ResponseEntity<List<CertificationResponse>> getAll(
                        @PathVariable Long cvId,
                        Authentication authentication) {

                String userId = authentication.getName();

                return ResponseEntity.ok(
                                certificationService.getByCv(
                                                cvId,
                                                userId));
        }

        // ============================================================
        // GET ONE
        // ============================================================

        @GetMapping("/{certificationId}")
        public ResponseEntity<CertificationResponse> get(
                        @PathVariable Long cvId,
                        @PathVariable Long certificationId,
                        Authentication authentication) {

                String userId = authentication.getName();

                return ResponseEntity.ok(
                                certificationService.get(
                                                cvId,
                                                certificationId,
                                                userId));
        }

        // ============================================================
        // UPDATE
        // ============================================================

        @PutMapping("/{certificationId}")
        public ResponseEntity<CertificationResponse> update(
                        @PathVariable Long cvId,
                        @PathVariable Long certificationId,
                        Authentication authentication,
                        @Valid @RequestBody CertificationRequest request) {

                String userId = authentication.getName();

                return ResponseEntity.ok(
                                certificationService.update(
                                                cvId,
                                                certificationId,
                                                userId,
                                                request));
        }

        // ============================================================
        // DELETE
        // ============================================================

        @DeleteMapping("/{certificationId}")
        public ResponseEntity<Void> delete(
                        @PathVariable Long cvId,
                        @PathVariable Long certificationId,
                        Authentication authentication) {

                String userId = authentication.getName();

                certificationService.delete(
                                cvId,
                                certificationId,
                                userId);

                return ResponseEntity
                                .noContent()
                                .build();
        }
}