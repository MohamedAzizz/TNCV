package com.tncv.cv_service.controller;

import com.tncv.cv_service.dto.ExperienceRequest;
import com.tncv.cv_service.dto.ExperienceResponse;
import com.tncv.cv_service.service.ExperienceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cvs/{cvId}/experiences")
public class ExperienceController {

    private final ExperienceService experienceService;

    public ExperienceController(
            ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    @PostMapping
    public ResponseEntity<ExperienceResponse> create(
            @PathVariable Long cvId,
            @Valid @RequestBody ExperienceRequest request) {

        ExperienceResponse response = experienceService.create(cvId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ExperienceResponse>> getAll(
            @PathVariable Long cvId) {

        return ResponseEntity.ok(
                experienceService.getByCv(cvId));
    }

    @GetMapping("/{experienceId}")
    public ResponseEntity<ExperienceResponse> get(
            @PathVariable Long cvId,
            @PathVariable Long experienceId) {

        return ResponseEntity.ok(
                experienceService.get(
                        cvId,
                        experienceId));
    }

    @PutMapping("/{experienceId}")
    public ResponseEntity<ExperienceResponse> update(
            @PathVariable Long cvId,
            @PathVariable Long experienceId,
            @Valid @RequestBody ExperienceRequest request) {

        return ResponseEntity.ok(
                experienceService.update(
                        cvId,
                        experienceId,
                        request));
    }

    @DeleteMapping("/{experienceId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long cvId,
            @PathVariable Long experienceId) {

        experienceService.delete(
                cvId,
                experienceId);

        return ResponseEntity.noContent().build();
    }
}