package com.tncv.cv_service.controller;

import com.tncv.cv_service.dto.LanguageRequest;
import com.tncv.cv_service.dto.LanguageResponse;
import com.tncv.cv_service.service.LanguageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cvs/{cvId}/languages")
public class LanguageController {

    private final LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @PostMapping
    public ResponseEntity<LanguageResponse> create(
            @PathVariable Long cvId,
            Authentication authentication,
            @Valid @RequestBody LanguageRequest request) {

        String userId = authentication.getName();

        LanguageResponse response = languageService.create(
                cvId,
                userId,
                request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<LanguageResponse>> getAll(
            @PathVariable Long cvId,
            Authentication authentication) {

        String userId = authentication.getName();

        return ResponseEntity.ok(
                languageService.getByCv(
                        cvId,
                        userId));
    }

    @GetMapping("/{languageId}")
    public ResponseEntity<LanguageResponse> get(
            @PathVariable Long cvId,
            @PathVariable Long languageId,
            Authentication authentication) {

        String userId = authentication.getName();

        return ResponseEntity.ok(
                languageService.get(
                        cvId,
                        languageId,
                        userId));
    }

    @PutMapping("/{languageId}")
    public ResponseEntity<LanguageResponse> update(
            @PathVariable Long cvId,
            @PathVariable Long languageId,
            Authentication authentication,
            @Valid @RequestBody LanguageRequest request) {

        String userId = authentication.getName();

        return ResponseEntity.ok(
                languageService.update(
                        cvId,
                        languageId,
                        userId,
                        request));
    }

    @DeleteMapping("/{languageId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long cvId,
            @PathVariable Long languageId,
            Authentication authentication) {

        String userId = authentication.getName();

        languageService.delete(
                cvId,
                languageId,
                userId);

        return ResponseEntity.noContent().build();
    }
}