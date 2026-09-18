package com.tncv.cv_service.controller;

import com.tncv.cv_service.dto.ProjectRequest;
import com.tncv.cv_service.dto.ProjectResponse;
import com.tncv.cv_service.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cvs/{cvId}/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> create(
            @PathVariable Long cvId,
            Authentication authentication,
            @Valid @RequestBody ProjectRequest request) {

        String userId = authentication.getName();

        ProjectResponse response = projectService.create(
                cvId,
                userId,
                request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAll(
            @PathVariable Long cvId,
            Authentication authentication) {

        String userId = authentication.getName();

        return ResponseEntity.ok(
                projectService.getByCv(
                        cvId,
                        userId));
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> get(
            @PathVariable Long cvId,
            @PathVariable Long projectId,
            Authentication authentication) {

        String userId = authentication.getName();

        return ResponseEntity.ok(
                projectService.get(
                        cvId,
                        projectId,
                        userId));
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> update(
            @PathVariable Long cvId,
            @PathVariable Long projectId,
            Authentication authentication,
            @Valid @RequestBody ProjectRequest request) {

        String userId = authentication.getName();

        return ResponseEntity.ok(
                projectService.update(
                        cvId,
                        projectId,
                        userId,
                        request));
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long cvId,
            @PathVariable Long projectId,
            Authentication authentication) {

        String userId = authentication.getName();

        projectService.delete(
                cvId,
                projectId,
                userId);

        return ResponseEntity.noContent().build();
    }
}