package com.tncv.cv_service.controller;

import com.tncv.cv_service.dto.ExperienceRequest;
import com.tncv.cv_service.dto.ExperienceResponse;
import com.tncv.cv_service.service.ExperienceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
                        Authentication authentication,
                        @Valid @RequestBody ExperienceRequest request) {

                String userId = authentication.getName();

                ExperienceResponse response = experienceService.create(
                                cvId,
                                userId,
                                request);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(response);
        }

        @GetMapping
        public ResponseEntity<List<ExperienceResponse>> getAll(
                        @PathVariable Long cvId,
                        Authentication authentication) {

                String userId = authentication.getName();

                return ResponseEntity.ok(
                                experienceService.getByCv(
                                                cvId,
                                                userId));
        }

        @GetMapping("/{experienceId}")
        public ResponseEntity<ExperienceResponse> get(
                        @PathVariable Long cvId,
                        @PathVariable Long experienceId,
                        Authentication authentication) {

                String userId = authentication.getName();

                return ResponseEntity.ok(
                                experienceService.get(
                                                cvId,
                                                experienceId,
                                                userId));
        }

        @PutMapping("/{experienceId}")
        public ResponseEntity<ExperienceResponse> update(
                        @PathVariable Long cvId,
                        @PathVariable Long experienceId,
                        Authentication authentication,
                        @Valid @RequestBody ExperienceRequest request) {

                String userId = authentication.getName();

                return ResponseEntity.ok(
                                experienceService.update(
                                                cvId,
                                                experienceId,
                                                userId,
                                                request));
        }

        @DeleteMapping("/{experienceId}")
        public ResponseEntity<Void> delete(
                        @PathVariable Long cvId,
                        @PathVariable Long experienceId,
                        Authentication authentication) {

                String userId = authentication.getName();

                experienceService.delete(
                                cvId,
                                experienceId,
                                userId);

                return ResponseEntity.noContent().build();
        }
}