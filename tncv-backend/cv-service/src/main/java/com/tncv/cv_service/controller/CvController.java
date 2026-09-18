// package com.tncv.cv_service.controller;

// import com.tncv.cv_service.dto.CvRequest;
// import com.tncv.cv_service.dto.CvResponse;
// import com.tncv.cv_service.service.CvService;
// import jakarta.validation.Valid;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.Authentication;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/api/cvs")
// public class CvController {

//     private final CvService cvService;

//     public CvController(CvService cvService) {
//         this.cvService = cvService;
//     }

//     // ============================================================
//     // CREATE
//     // ============================================================

//     @PostMapping
//     public ResponseEntity<CvResponse> createCv(
//             Authentication authentication,
//             @Valid @RequestBody CvRequest request) {

//         String userId = authentication.getName();

//         CvResponse response = cvService.createCv(
//                 userId,
//                 request);

//         return ResponseEntity
//                 .status(HttpStatus.CREATED)
//                 .body(response);
//     }

//     // ============================================================
//     // GET ALL USER CVS
//     // ============================================================

//     @GetMapping
//     public ResponseEntity<List<CvResponse>> getUserCvs(
//             Authentication authentication) {

//         String userId = authentication.getName();

//         return ResponseEntity.ok(
//                 cvService.getUserCvs(userId));
//     }

//     // ============================================================
//     // GET ONE CV
//     // ============================================================

//     @GetMapping("/{id}")
//     public ResponseEntity<CvResponse> getCv(
//             @PathVariable Long id,
//             Authentication authentication) {

//         String userId = authentication.getName();

//         return ResponseEntity.ok(
//                 cvService.getCv(id, userId));
//     }

//     // ============================================================
//     // UPDATE
//     // ============================================================

//     @PutMapping("/{id}")
//     public ResponseEntity<CvResponse> updateCv(
//             @PathVariable Long id,
//             Authentication authentication,
//             @Valid @RequestBody CvRequest request) {

//         String userId = authentication.getName();

//         return ResponseEntity.ok(
//                 cvService.updateCv(
//                         id,
//                         userId,
//                         request));
//     }

//     // ============================================================
//     // DELETE
//     // ============================================================

//     @DeleteMapping("/{id}")
//     public ResponseEntity<Void> deleteCv(
//             @PathVariable Long id,
//             Authentication authentication) {

//         String userId = authentication.getName();

//         cvService.deleteCv(
//                 id,
//                 userId);

//         return ResponseEntity
//                 .noContent()
//                 .build();
//     }
// }
package com.tncv.cv_service.controller;

import com.tncv.cv_service.dto.CvRequest;
import com.tncv.cv_service.dto.CvResponse;
import com.tncv.cv_service.service.CvService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cvs")
public class CvController {

    private final CvService cvService;

    public CvController(CvService cvService) {
        this.cvService = cvService;
    }

    // ============================================================
    // CREATE CV
    // POST /api/cvs
    // ============================================================

    @PostMapping
    public ResponseEntity<CvResponse> createCv(
            Authentication authentication,
            @Valid @RequestBody CvRequest request) {

        // Récupération du Keycloak user ID depuis le JWT
        String userId = authentication.getName();

        CvResponse response = cvService.createCv(
                userId,
                request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // ============================================================
    // GET ALL MY CVS
    // GET /api/cvs
    // ============================================================

    @GetMapping
    public ResponseEntity<List<CvResponse>> getUserCvs(
            Authentication authentication) {

        // Utilisateur connecté
        String userId = authentication.getName();

        List<CvResponse> cvs = cvService.getUserCvs(userId);

        return ResponseEntity.ok(cvs);
    }

    // ============================================================
    // GET MY CVS - ENDPOINT H7
    // GET /api/cvs/me
    // ============================================================

    @GetMapping("/me")
    public ResponseEntity<List<CvResponse>> getMyCvs(
            Authentication authentication) {

        // Récupération du userId depuis le JWT
        String userId = authentication.getName();

        List<CvResponse> cvs = cvService.getUserCvs(userId);

        return ResponseEntity.ok(cvs);
    }

    // ============================================================
    // GET ONE CV
    // GET /api/cvs/{id}
    // ============================================================

    @GetMapping("/{id}")
    public ResponseEntity<CvResponse> getCv(
            @PathVariable Long id,
            Authentication authentication) {

        // Récupération du userId depuis le JWT
        String userId = authentication.getName();

        CvResponse response = cvService.getCv(
                id,
                userId);

        return ResponseEntity.ok(response);
    }

    // ============================================================
    // UPDATE MY CV
    // PUT /api/cvs/{id}
    // ============================================================

    @PutMapping("/{id}")
    public ResponseEntity<CvResponse> updateCv(
            @PathVariable Long id,
            Authentication authentication,
            @Valid @RequestBody CvRequest request) {

        // Récupération du userId depuis le JWT
        String userId = authentication.getName();

        CvResponse response = cvService.updateCv(
                id,
                userId,
                request);

        return ResponseEntity.ok(response);
    }

    // ============================================================
    // DELETE MY CV
    // DELETE /api/cvs/{id}
    // ============================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCv(
            @PathVariable Long id,
            Authentication authentication) {

        // Récupération du userId depuis le JWT
        String userId = authentication.getName();

        cvService.deleteCv(
                id,
                userId);

        return ResponseEntity
                .noContent()
                .build();
    }
}