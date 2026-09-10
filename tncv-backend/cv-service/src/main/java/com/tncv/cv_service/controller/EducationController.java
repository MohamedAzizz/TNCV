package com.tncv.cv_service.controller;

import com.tncv.cv_service.dto.EducationRequest;
import com.tncv.cv_service.dto.EducationResponse;
import com.tncv.cv_service.service.EducationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cvs/{cvId}/educations")
public class EducationController {

    private final EducationService educationService;

    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public EducationController(
            EducationService educationService) {

        this.educationService = educationService;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @PostMapping
    public ResponseEntity<EducationResponse> create(
            @PathVariable Long cvId,
            @Valid @RequestBody EducationRequest request) {

        EducationResponse response = educationService.create(
                cvId,
                request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // ============================================================
    // GET ALL
    // ============================================================

    @GetMapping
    public ResponseEntity<List<EducationResponse>> getAll(
            @PathVariable Long cvId) {

        return ResponseEntity.ok(
                educationService.getByCv(cvId));
    }

    // ============================================================
    // GET ONE
    // ============================================================

    @GetMapping("/{educationId}")
    public ResponseEntity<EducationResponse> get(
            @PathVariable Long cvId,
            @PathVariable Long educationId) {

        return ResponseEntity.ok(
                educationService.get(
                        cvId,
                        educationId));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @PutMapping("/{educationId}")
    public ResponseEntity<EducationResponse> update(
            @PathVariable Long cvId,
            @PathVariable Long educationId,
            @Valid @RequestBody EducationRequest request) {

        return ResponseEntity.ok(
                educationService.update(
                        cvId,
                        educationId,
                        request));
    }

    // ============================================================
    // DELETE
    // ============================================================

    @DeleteMapping("/{educationId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long cvId,
            @PathVariable Long educationId) {

        educationService.delete(
                cvId,
                educationId);

        return ResponseEntity
                .noContent()
                .build();
    }
}