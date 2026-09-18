package com.tncv.cv_service.controller;

import com.tncv.cv_service.dto.InterestRequest;
import com.tncv.cv_service.dto.InterestResponse;
import com.tncv.cv_service.service.InterestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cvs/{cvId}/interests")
public class InterestController {

        private final InterestService interestService;

        public InterestController(InterestService interestService) {
                this.interestService = interestService;
        }

        @PostMapping
        public ResponseEntity<InterestResponse> create(
                        @PathVariable Long cvId,
                        Authentication authentication,
                        @Valid @RequestBody InterestRequest request) {

                String userId = authentication.getName();

                InterestResponse response = interestService.create(
                                cvId,
                                userId,
                                request);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(response);
        }

        @GetMapping
        public ResponseEntity<List<InterestResponse>> getAll(
                        @PathVariable Long cvId,
                        Authentication authentication) {

                String userId = authentication.getName();

                return ResponseEntity.ok(
                                interestService.getByCv(
                                                cvId,
                                                userId));
        }

        @GetMapping("/{interestId}")
        public ResponseEntity<InterestResponse> get(
                        @PathVariable Long cvId,
                        @PathVariable Long interestId,
                        Authentication authentication) {

                String userId = authentication.getName();

                return ResponseEntity.ok(
                                interestService.get(
                                                cvId,
                                                interestId,
                                                userId));
        }

        @PutMapping("/{interestId}")
        public ResponseEntity<InterestResponse> update(
                        @PathVariable Long cvId,
                        @PathVariable Long interestId,
                        Authentication authentication,
                        @Valid @RequestBody InterestRequest request) {

                String userId = authentication.getName();

                return ResponseEntity.ok(
                                interestService.update(
                                                cvId,
                                                interestId,
                                                userId,
                                                request));
        }

        @DeleteMapping("/{interestId}")
        public ResponseEntity<Void> delete(
                        @PathVariable Long cvId,
                        @PathVariable Long interestId,
                        Authentication authentication) {

                String userId = authentication.getName();

                interestService.delete(
                                cvId,
                                interestId,
                                userId);

                return ResponseEntity.noContent().build();
        }
}