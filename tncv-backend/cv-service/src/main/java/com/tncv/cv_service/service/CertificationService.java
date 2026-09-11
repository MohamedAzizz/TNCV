package com.tncv.cv_service.service;

import com.tncv.cv_service.dto.CertificationRequest;
import com.tncv.cv_service.dto.CertificationResponse;
import com.tncv.cv_service.entity.Certification;
import com.tncv.cv_service.entity.Cv;
import com.tncv.cv_service.exception.ResourceNotFoundException;
import com.tncv.cv_service.repository.CertificationRepository;
import com.tncv.cv_service.repository.CvRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificationService {

    private final CertificationRepository certificationRepository;
    private final CvRepository cvRepository;

    public CertificationService(
            CertificationRepository certificationRepository,
            CvRepository cvRepository) {

        this.certificationRepository = certificationRepository;
        this.cvRepository = cvRepository;
    }

    // ============================================================
    // CREATE
    // ============================================================

    public CertificationResponse create(
            Long cvId,
            CertificationRequest request) {

        Cv cv = cvRepository
                .findById(cvId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CV introuvable avec l'id : " + cvId));

        Certification certification = new Certification();

        certification.setName(
                request.getName());

        certification.setIssuingOrganization(
                request.getIssuingOrganization());

        certification.setIssueDate(
                request.getIssueDate());

        certification.setExpirationDate(
                request.getExpirationDate());

        certification.setNoExpiration(
                request.isNoExpiration());

        certification.setCredentialId(
                request.getCredentialId());

        certification.setCredentialUrl(
                request.getCredentialUrl());

        certification.setDescription(
                request.getDescription());

        // Association avec le CV
        certification.setCv(cv);

        Certification savedCertification = certificationRepository.save(certification);

        return new CertificationResponse(
                savedCertification);
    }

    // ============================================================
    // GET ALL
    // ============================================================

    public List<CertificationResponse> getByCv(
            Long cvId) {

        return certificationRepository
                .findByCvId(cvId)
                .stream()
                .map(CertificationResponse::new)
                .toList();
    }

    // ============================================================
    // GET ONE
    // ============================================================

    public CertificationResponse get(
            Long cvId,
            Long certificationId) {

        Certification certification = certificationRepository
                .findByIdAndCvId(
                        certificationId,
                        cvId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Certification introuvable avec l'id : "
                                + certificationId));

        return new CertificationResponse(
                certification);
    }

    // ============================================================
    // UPDATE
    // ============================================================

    public CertificationResponse update(
            Long cvId,
            Long certificationId,
            CertificationRequest request) {

        Certification certification = certificationRepository
                .findByIdAndCvId(
                        certificationId,
                        cvId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Certification introuvable avec l'id : "
                                + certificationId));

        certification.setName(
                request.getName());

        certification.setIssuingOrganization(
                request.getIssuingOrganization());

        certification.setIssueDate(
                request.getIssueDate());

        certification.setExpirationDate(
                request.getExpirationDate());

        certification.setNoExpiration(
                request.isNoExpiration());

        certification.setCredentialId(
                request.getCredentialId());

        certification.setCredentialUrl(
                request.getCredentialUrl());

        certification.setDescription(
                request.getDescription());

        Certification updatedCertification = certificationRepository.save(
                certification);

        return new CertificationResponse(
                updatedCertification);
    }

    // ============================================================
    // DELETE
    // ============================================================

    public void delete(
            Long cvId,
            Long certificationId) {

        Certification certification = certificationRepository
                .findByIdAndCvId(
                        certificationId,
                        cvId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Certification introuvable avec l'id : "
                                + certificationId));

        certificationRepository.delete(
                certification);
    }
}