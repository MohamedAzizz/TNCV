package com.tncv.cv_service.service;

import com.tncv.cv_service.dto.CvDownload;
import com.tncv.cv_service.dto.CvFileResponse;
import com.tncv.cv_service.dto.CvRequest;
import com.tncv.cv_service.dto.CvResponse;
import com.tncv.cv_service.entity.Cv;
import com.tncv.cv_service.exception.FileTooLargeException;
import com.tncv.cv_service.exception.InvalidFileTypeException;
import com.tncv.cv_service.exception.ResourceNotFoundException;
import com.tncv.cv_service.exception.StorageException;
import com.tncv.cv_service.exception.UnauthorizedFileAccessException;
import com.tncv.cv_service.repository.CvRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class CvService {

    private final CvRepository cvRepository;
    private final MinioService minioService;

    @Value("${minio.bucket.cvs:tncv-cvs}")
    private String cvsBucket;

    @Value("${tncv.cv-file.max-size-bytes:10485760}")
    private long maxFileSizeBytes;

    public CvService(CvRepository cvRepository, MinioService minioService) {
        this.cvRepository = cvRepository;
        this.minioService = minioService;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @Transactional
    public CvResponse createCv(String userId, CvRequest request) {

        Cv cv = new Cv();
        cv.setUserId(userId);
        cv.setTitle(request.getTitle());
        cv.setFullName(request.getFullName());
        cv.setEmail(request.getEmail());
        cv.setPhone(request.getPhone());
        cv.setAddress(request.getAddress());
        cv.setLinkedin(request.getLinkedin());
        cv.setGithub(request.getGithub());
        cv.setSummary(request.getSummary());

        Cv savedCv = cvRepository.save(cv);
        log.info("Created CV with id={} for user={}", savedCv.getId(), userId);

        return new CvResponse(savedCv);
    }

    // ============================================================
    // GET ALL USER CVS
    // ============================================================

    public List<CvResponse> getUserCvs(String userId) {

        return cvRepository
                .findByUserId(userId)
                .stream()
                .map(CvResponse::new)
                .toList();
    }

    // ============================================================
    // GET ONE CV
    // ============================================================

    public CvResponse getCv(Long id, String userId) {

        Cv cv = cvRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CV introuvable avec l'id : " + id));

        if (!cv.getUserId().equals(userId)) {
            throw new UnauthorizedFileAccessException("Accès refusé : vous n'êtes pas propriétaire de ce CV");
        }

        return new CvResponse(cv);
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Transactional
    public CvResponse updateCv(Long id, String userId, CvRequest request) {

        Cv cv = cvRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CV introuvable avec l'id : " + id));

        if (!cv.getUserId().equals(userId)) {
            throw new UnauthorizedFileAccessException("Accès refusé : vous n'êtes pas propriétaire de ce CV");
        }

        cv.setTitle(request.getTitle());
        cv.setFullName(request.getFullName());
        cv.setEmail(request.getEmail());
        cv.setPhone(request.getPhone());
        cv.setAddress(request.getAddress());
        cv.setLinkedin(request.getLinkedin());
        cv.setGithub(request.getGithub());
        cv.setSummary(request.getSummary());

        Cv updatedCv = cvRepository.save(cv);
        log.info("Updated CV id={} for user={}", id, userId);

        return new CvResponse(updatedCv);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Transactional
    public void deleteCv(Long id, String userId) {

        Cv cv = cvRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CV introuvable avec l'id : " + id));

        if (!cv.getUserId().equals(userId)) {
            throw new UnauthorizedFileAccessException("Accès refusé : vous n'êtes pas propriétaire de ce CV");
        }

        // Clean up MinIO file if present
        if (cv.getFileObjectName() != null) {
            try {
                minioService.deleteFile(cvsBucket, cv.getFileObjectName());
                log.info("Deleted CV file from MinIO on CV deletion: {}", cv.getFileObjectName());
            } catch (Exception ex) {
                log.warn("Could not delete MinIO file during CV deletion: {}", cv.getFileObjectName(), ex);
            }
        }

        cvRepository.delete(cv);
        log.info("Deleted CV id={} for user={}", id, userId);
    }

    // ============================================================
    // UPLOAD CV FILE (PDF / DOCX)
    // ============================================================

    @Transactional
    public CvFileResponse uploadCvFile(String userId, Long cvId, String title, MultipartFile file) {

        validateCvFile(file);

        Cv cv;
        if (cvId != null) {
            cv = cvRepository.findById(cvId)
                    .orElseThrow(() -> new ResourceNotFoundException("CV introuvable avec l'id : " + cvId));
            if (!cv.getUserId().equals(userId)) {
                throw new UnauthorizedFileAccessException("Accès refusé : vous n'êtes pas propriétaire de ce CV");
            }
            if (title != null && !title.isBlank()) {
                cv.setTitle(title);
            }
        } else {
            cv = new Cv();
            cv.setUserId(userId);
            cv.setTitle(title != null && !title.isBlank() ? title : "CV - " + file.getOriginalFilename());
            cv = cvRepository.save(cv);
        }

        String originalName = file.getOriginalFilename();
        String extension = (originalName != null && originalName.toLowerCase().endsWith(".docx")) ? ".docx" : ".pdf";
        String uuid = UUID.randomUUID().toString();
        String storedFileName = uuid + extension;
        String objectName = "users/" + userId + "/cvs/" + cv.getId() + "/" + storedFileName;
        String oldObjectName = cv.getFileObjectName();

        String newlyUploadedObjectName = null;
        try {
            log.info("Uploading CV file to MinIO: user={}, cvId={}, object={}", userId, cv.getId(), objectName);
            minioService.uploadFile(
                    cvsBucket,
                    objectName,
                    file.getInputStream(),
                    file.getContentType(),
                    file.getSize()
            );
            newlyUploadedObjectName = objectName;

            cv.setOriginalFileName(originalName);
            cv.setStoredFileName(storedFileName);
            cv.setFileObjectName(objectName);
            cv.setFileType(file.getContentType());
            cv.setFileSize(file.getSize());

            cvRepository.save(cv);

            // Clean up old object in MinIO if one existed
            if (oldObjectName != null && !oldObjectName.equals(objectName)) {
                try {
                    minioService.deleteFile(cvsBucket, oldObjectName);
                    log.info("Previous CV file removed from MinIO: {}", oldObjectName);
                } catch (Exception ex) {
                    log.warn("Could not delete previous CV file from MinIO: {}", oldObjectName, ex);
                }
            }

            log.info("CV file successfully uploaded and metadata saved for cvId={}", cv.getId());
            return CvFileResponse.of(
                    cv.getId(),
                    cv.getOriginalFileName(),
                    cv.getFileType(),
                    cv.getFileSize(),
                    "Fichier CV téléversé avec succès"
            );

        } catch (Exception e) {
            // Rollback uploaded MinIO file
            if (newlyUploadedObjectName != null) {
                try {
                    minioService.deleteFile(cvsBucket, newlyUploadedObjectName);
                    log.info("Rollback: deleted MinIO object {} following error", newlyUploadedObjectName);
                } catch (Exception rollbackEx) {
                    log.error("Failed rollback for MinIO object {}", newlyUploadedObjectName, rollbackEx);
                }
            }
            if (e instanceof StorageException se) {
                throw se;
            }
            throw new StorageException("Erreur lors de l'enregistrement du fichier CV", e);
        }
    }

    // ============================================================
    // DOWNLOAD CV FILE
    // ============================================================

    public CvDownload downloadCvFile(Long cvId, String userId) {

        Cv cv = cvRepository.findById(cvId)
                .orElseThrow(() -> new ResourceNotFoundException("CV introuvable avec l'id : " + cvId));

        if (!cv.getUserId().equals(userId)) {
            throw new UnauthorizedFileAccessException("Accès refusé : vous n'êtes pas autorisé à télécharger ce CV");
        }

        if (cv.getFileObjectName() == null) {
            throw new ResourceNotFoundException("Aucun fichier n'est associé à ce CV (id: " + cvId + ")");
        }

        InputStream inputStream = minioService.downloadFile(cvsBucket, cv.getFileObjectName());
        String defaultExt = (cv.getFileType() != null && cv.getFileType().contains("word")) ? ".docx" : ".pdf";
        String filename = cv.getOriginalFileName() != null ? cv.getOriginalFileName() : "cv-" + cvId + defaultExt;

        return new CvDownload(
                inputStream,
                filename,
                cv.getFileType() != null ? cv.getFileType() : "application/pdf",
                cv.getFileSize() != null ? cv.getFileSize() : -1
        );
    }

    // ============================================================
    // DELETE CV FILE
    // ============================================================

    @Transactional
    public void deleteCvFile(Long cvId, String userId) {

        Cv cv = cvRepository.findById(cvId)
                .orElseThrow(() -> new ResourceNotFoundException("CV introuvable avec l'id : " + cvId));

        if (!cv.getUserId().equals(userId)) {
            throw new UnauthorizedFileAccessException("Accès refusé : vous n'êtes pas autorisé à modifier ce CV");
        }

        if (cv.getFileObjectName() == null) {
            log.info("No file to delete for cvId: {}", cvId);
            return;
        }

        minioService.deleteFile(cvsBucket, cv.getFileObjectName());

        cv.setFileObjectName(null);
        cv.setStoredFileName(null);
        cv.setOriginalFileName(null);
        cv.setFileType(null);
        cv.setFileSize(null);

        cvRepository.save(cv);
        log.info("CV file deleted successfully for cvId: {}", cvId);
    }

    // ============================================================
    // VALIDATION
    // ============================================================

    private void validateCvFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new InvalidFileTypeException("Le fichier est vide ou absent");
        }

        if (file.getSize() > maxFileSizeBytes) {
            throw new FileTooLargeException(
                    "Le fichier dépasse la taille maximale autorisée (" + (maxFileSizeBytes / 1024 / 1024) + " MB)"
            );
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new InvalidFileTypeException("Nom de fichier invalide");
        }

        String lowerFilename = originalFilename.toLowerCase();
        boolean isPdfExt = lowerFilename.endsWith(".pdf");
        boolean isDocxExt = lowerFilename.endsWith(".docx");

        if (!isPdfExt && !isDocxExt) {
            throw new InvalidFileTypeException("Extension non autorisée. Formats acceptés : .pdf, .docx");
        }

        String contentType = file.getContentType();
        if (contentType == null) {
            throw new InvalidFileTypeException("Type de contenu (Content-Type) absent");
        }

        // Validate MIME type
        if (isPdfExt && !contentType.equalsIgnoreCase("application/pdf")) {
            throw new InvalidFileTypeException("Le Content-Type (" + contentType + ") ne correspond pas à un fichier PDF");
        }
        if (isDocxExt && !contentType.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
            throw new InvalidFileTypeException("Le Content-Type (" + contentType + ") ne correspond pas à un document Word DOCX");
        }

        // Validate Magic Bytes (Robust binary content check)
        try (InputStream is = file.getInputStream()) {
            byte[] header = new byte[4];
            int read = is.read(header);
            if (read < 4) {
                throw new InvalidFileTypeException("Fichier corrompu ou taille insuffisante");
            }

            if (isPdfExt) {
                // PDF magic bytes: %PDF -> 0x25, 0x50, 0x44, 0x46
                if (header[0] != 0x25 || header[1] != 0x50 || header[2] != 0x44 || header[3] != 0x46) {
                    throw new InvalidFileTypeException("Contenu invalide : la signature binaire ne correspond pas à un fichier PDF");
                }
            } else {
                // DOCX magic bytes: PK\x03\x04 -> 0x50, 0x4B, 0x03, 0x04
                if (header[0] != 0x50 || header[1] != 0x4B || header[2] != 0x03 || header[3] != 0x04) {
                    throw new InvalidFileTypeException("Contenu invalide : la signature binaire ne correspond pas à un document DOCX");
                }
            }
        } catch (IOException e) {
            throw new StorageException("Impossible de lire le fichier pour validation", e);
        }
    }
}