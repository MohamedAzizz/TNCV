package com.tncv.cv_service.controller;

import com.tncv.cv_service.dto.EducationRequest;
import com.tncv.cv_service.dto.EducationResponse;
import com.tncv.cv_service.service.EducationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cvs/{cvId}/educations")
public class EducationController {

        private final EducationService educationService;

        public EducationController(
                        EducationService educationService) {

                this.educationService = educationService;
        }

        @PostMapping
        public ResponseEntity<EducationResponse> create(
                        @PathVariable Long cvId,
                        Authentication authentication,
                        @Valid @RequestBody EducationRequest request) {

                String userId = authentication.getName();

                EducationResponse response = educationService.create(
                                cvId,
                                userId,
                                request);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(response);
        }

        @GetMapping
        public ResponseEntity<List<EducationResponse>> getAll(
                        @PathVariable Long cvId,
                        Authentication authentication) {

                String userId = authentication.getName();

                return ResponseEntity.ok(
                                educationService.getByCv(
                                                cvId,
                                                userId));
        }

        @GetMapping("/{educationId}")
        public ResponseEntity<EducationResponse> get(
                        @PathVariable Long cvId,
                        @PathVariable Long educationId,
                        Authentication authentication) {

                String userId = authentication.getName();

                return ResponseEntity.ok(
                                educationService.get(
                                                cvId,
                                                educationId,
                                                userId));
        }

        @PutMapping("/{educationId}")
        public ResponseEntity<EducationResponse> update(
                        @PathVariable Long cvId,
                        @PathVariable Long educationId,
                        Authentication authentication,
                        @Valid @RequestBody EducationRequest request) {

                String userId = authentication.getName();

                return ResponseEntity.ok(
                                educationService.update(
                                                cvId,
                                                educationId,
                                                userId,
                                                request));
        }

        @DeleteMapping("/{educationId}")
        public ResponseEntity<Void> delete(
                        @PathVariable Long cvId,
                        @PathVariable Long educationId,
                        Authentication authentication) {

                String userId = authentication.getName();

                educationService.delete(
                                cvId,
                                educationId,
                                userId);

                return ResponseEntity
                                .noContent()
                                .build();
        }
}