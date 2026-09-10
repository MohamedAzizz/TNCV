package com.tncv.cv_service.controller;

import com.tncv.cv_service.dto.SkillRequest;
import com.tncv.cv_service.dto.SkillResponse;
import com.tncv.cv_service.service.SkillService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cvs/{cvId}/skills")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<SkillResponse> create(
            @PathVariable Long cvId,
            @Valid @RequestBody SkillRequest request) {

        return ResponseEntity.ok(
                skillService.create(cvId, request));
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<SkillResponse>> getAll(
            @PathVariable Long cvId) {

        return ResponseEntity.ok(
                skillService.getByCv(cvId));
    }

    // GET ONE
    @GetMapping("/{skillId}")
    public ResponseEntity<SkillResponse> get(
            @PathVariable Long cvId,
            @PathVariable Long skillId) {

        return ResponseEntity.ok(
                skillService.get(cvId, skillId));
    }

    // UPDATE
    @PutMapping("/{skillId}")
    public ResponseEntity<SkillResponse> update(
            @PathVariable Long cvId,
            @PathVariable Long skillId,
            @Valid @RequestBody SkillRequest request) {

        return ResponseEntity.ok(
                skillService.update(cvId, skillId, request));
    }

    // DELETE
    @DeleteMapping("/{skillId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long cvId,
            @PathVariable Long skillId) {

        skillService.delete(cvId, skillId);

        return ResponseEntity.noContent().build();
    }
}