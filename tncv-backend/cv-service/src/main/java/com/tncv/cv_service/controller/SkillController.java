package com.tncv.cv_service.controller;

import com.tncv.cv_service.dto.SkillRequest;
import com.tncv.cv_service.dto.SkillResponse;
import com.tncv.cv_service.service.SkillService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cvs/{cvId}/skills")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping
    public ResponseEntity<SkillResponse> create(
            @PathVariable Long cvId,
            Authentication authentication,
            @Valid @RequestBody SkillRequest request) {

        String userId = authentication.getName();

        SkillResponse response = skillService.create(
                cvId,
                userId,
                request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<SkillResponse>> getAll(
            @PathVariable Long cvId,
            Authentication authentication) {

        String userId = authentication.getName();

        return ResponseEntity.ok(
                skillService.getByCv(
                        cvId,
                        userId));
    }

    @GetMapping("/{skillId}")
    public ResponseEntity<SkillResponse> get(
            @PathVariable Long cvId,
            @PathVariable Long skillId,
            Authentication authentication) {

        String userId = authentication.getName();

        return ResponseEntity.ok(
                skillService.get(
                        cvId,
                        skillId,
                        userId));
    }

    @PutMapping("/{skillId}")
    public ResponseEntity<SkillResponse> update(
            @PathVariable Long cvId,
            @PathVariable Long skillId,
            Authentication authentication,
            @Valid @RequestBody SkillRequest request) {

        String userId = authentication.getName();

        return ResponseEntity.ok(
                skillService.update(
                        cvId,
                        skillId,
                        userId,
                        request));
    }

    @DeleteMapping("/{skillId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long cvId,
            @PathVariable Long skillId,
            Authentication authentication) {

        String userId = authentication.getName();

        skillService.delete(
                cvId,
                skillId,
                userId);

        return ResponseEntity.noContent().build();
    }
}