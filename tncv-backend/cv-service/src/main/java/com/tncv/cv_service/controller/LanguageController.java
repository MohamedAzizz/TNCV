package com.tncv.cv_service.controller;

import com.tncv.cv_service.dto.LanguageRequest;
import com.tncv.cv_service.dto.LanguageResponse;
import com.tncv.cv_service.service.LanguageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cvs/{cvId}/languages")
public class LanguageController {

    private final LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<LanguageResponse> create(
            @PathVariable Long cvId,
            @Valid @RequestBody LanguageRequest request) {

        return ResponseEntity.ok(
                languageService.create(cvId, request));
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<LanguageResponse>> getAll(
            @PathVariable Long cvId) {

        return ResponseEntity.ok(
                languageService.getByCv(cvId));
    }

    // GET ONE
    @GetMapping("/{languageId}")
    public ResponseEntity<LanguageResponse> get(
            @PathVariable Long cvId,
            @PathVariable Long languageId) {

        return ResponseEntity.ok(
                languageService.get(cvId, languageId));
    }

    // UPDATE
    @PutMapping("/{languageId}")
    public ResponseEntity<LanguageResponse> update(
            @PathVariable Long cvId,
            @PathVariable Long languageId,
            @Valid @RequestBody LanguageRequest request) {

        return ResponseEntity.ok(
                languageService.update(
                        cvId,
                        languageId,
                        request));
    }

    // DELETE
    @DeleteMapping("/{languageId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long cvId,
            @PathVariable Long languageId) {

        languageService.delete(cvId, languageId);

        return ResponseEntity.noContent().build();
    }
}