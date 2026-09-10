package com.tncv.cv_service.controller;

import com.tncv.cv_service.dto.CertificationRequest;
import com.tncv.cv_service.dto.CertificationResponse;
import com.tncv.cv_service.service.CertificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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

    // CREATE
    @PostMapping
    public ResponseEntity<CertificationResponse> create(
            @PathVariable Long cvId,
            @Valid @RequestBody CertificationRequest request) {

        return ResponseEntity.ok(
                certificationService.create(
                        cvId,
                        request));
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<CertificationResponse>> getAll(
            @PathVariable Long cvId) {

        return ResponseEntity.ok(
                certificationService.getByCv(cvId));
    }

    // GET ONE
    @GetMapping("/{certificationId}")
    public ResponseEntity<CertificationResponse> get(
            @PathVariable Long cvId,
            @PathVariable Long certificationId) {

        return ResponseEntity.ok(
                certificationService.get(
                        cvId,
                        certificationId));
    }

    // UPDATE
    @PutMapping("/{certificationId}")
    public ResponseEntity<CertificationResponse> update(
            @PathVariable Long cvId,
            @PathVariable Long certificationId,
            @Valid @RequestBody CertificationRequest request) {

        return ResponseEntity.ok(
                certificationService.update(
                        cvId,
                        certificationId,
                        request));
    }

    // DELETE
    @DeleteMapping("/{certificationId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long cvId,
            @PathVariable Long certificationId) {

        certificationService.delete(
                cvId,
                certificationId);

        return ResponseEntity.noContent().build();
    }
}