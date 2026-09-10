package com.tncv.cv_service.controller;

import com.tncv.cv_service.dto.ProjectRequest;
import com.tncv.cv_service.dto.ProjectResponse;
import com.tncv.cv_service.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cvs/{cvId}/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<ProjectResponse> create(
            @PathVariable Long cvId,
            @Valid @RequestBody ProjectRequest request) {

        return ResponseEntity.ok(
                projectService.create(cvId, request));
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAll(
            @PathVariable Long cvId) {

        return ResponseEntity.ok(
                projectService.getByCv(cvId));
    }

    // GET ONE
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> get(
            @PathVariable Long cvId,
            @PathVariable Long projectId) {

        return ResponseEntity.ok(
                projectService.get(cvId, projectId));
    }

    // UPDATE
    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> update(
            @PathVariable Long cvId,
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectRequest request) {

        return ResponseEntity.ok(
                projectService.update(
                        cvId,
                        projectId,
                        request));
    }

    // DELETE
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long cvId,
            @PathVariable Long projectId) {

        projectService.delete(cvId, projectId);

        return ResponseEntity.noContent().build();
    }
}