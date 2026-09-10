package com.tncv.cv_service.controller;

import com.tncv.cv_service.dto.InterestRequest;
import com.tncv.cv_service.dto.InterestResponse;
import com.tncv.cv_service.service.InterestService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cvs/{cvId}/interests")
public class InterestController {

    private final InterestService interestService;

    public InterestController(
            InterestService interestService) {
        this.interestService = interestService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<InterestResponse> create(
            @PathVariable Long cvId,
            @Valid @RequestBody InterestRequest request) {

        return ResponseEntity.ok(
                interestService.create(
                        cvId,
                        request));
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<InterestResponse>> getAll(
            @PathVariable Long cvId) {

        return ResponseEntity.ok(
                interestService.getByCv(cvId));
    }

    // GET ONE
    @GetMapping("/{interestId}")
    public ResponseEntity<InterestResponse> get(
            @PathVariable Long cvId,
            @PathVariable Long interestId) {

        return ResponseEntity.ok(
                interestService.get(
                        cvId,
                        interestId));
    }

    // UPDATE
    @PutMapping("/{interestId}")
    public ResponseEntity<InterestResponse> update(
            @PathVariable Long cvId,
            @PathVariable Long interestId,
            @Valid @RequestBody InterestRequest request) {

        return ResponseEntity.ok(
                interestService.update(
                        cvId,
                        interestId,
                        request));
    }

    // DELETE
    @DeleteMapping("/{interestId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long cvId,
            @PathVariable Long interestId) {

        interestService.delete(
                cvId,
                interestId);

        return ResponseEntity.noContent().build();
    }
}