package com.tncv.cv_service.controller;

import com.tncv.cv_service.dto.CvRequest;
import com.tncv.cv_service.dto.CvResponse;
import com.tncv.cv_service.service.CvService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cvs")
public class CvController {

    private final CvService cvService;

    public CvController(CvService cvService) {
        this.cvService = cvService;
    }

    @PostMapping
    public ResponseEntity<CvResponse> createCv(
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody CvRequest request) {

        CvResponse response = cvService.createCv(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<CvResponse>> getUserCvs(
            @RequestHeader("X-User-Id") String userId) {

        return ResponseEntity.ok(
                cvService.getUserCvs(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CvResponse> getCv(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") String userId) {

        return ResponseEntity.ok(
                cvService.getCv(id, userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CvResponse> updateCv(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody CvRequest request) {

        return ResponseEntity.ok(
                cvService.updateCv(id, userId, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCv(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") String userId) {

        cvService.deleteCv(id, userId);

        return ResponseEntity.noContent().build();
    }
}