package com.tncv.cv_service.service;

import com.tncv.cv_service.dto.EducationRequest;
import com.tncv.cv_service.dto.EducationResponse;
import com.tncv.cv_service.entity.Cv;
import com.tncv.cv_service.entity.Education;
import com.tncv.cv_service.exception.ResourceNotFoundException;
import com.tncv.cv_service.repository.CvRepository;
import com.tncv.cv_service.repository.EducationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EducationService {

    private final EducationRepository educationRepository;
    private final CvRepository cvRepository;

    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public EducationService(
            EducationRepository educationRepository,
            CvRepository cvRepository) {

        this.educationRepository = educationRepository;
        this.cvRepository = cvRepository;
    }

    // ============================================================
    // CREATE
    // ============================================================

    public EducationResponse create(
            Long cvId,
            EducationRequest request) {

        Cv cv = cvRepository
                .findById(cvId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CV introuvable avec l'id : " + cvId));

        Education education = new Education();

        education.setInstitution(request.getInstitution());
        education.setDegree(request.getDegree());
        education.setFieldOfStudy(request.getFieldOfStudy());
        education.setLocation(request.getLocation());
        education.setStartDate(request.getStartDate());
        education.setEndDate(request.getEndDate());
        education.setCurrent(request.isCurrent());
        education.setDescription(request.getDescription());

        education.setCv(cv);

        Education saved = educationRepository.save(education);

        return new EducationResponse(saved);
    }

    // ============================================================
    // GET ALL
    // ============================================================

    public List<EducationResponse> getByCv(Long cvId) {

        return educationRepository
                .findByCvId(cvId)
                .stream()
                .map(EducationResponse::new)
                .toList();
    }

    // ============================================================
    // GET ONE
    // ============================================================

    public EducationResponse get(
            Long cvId,
            Long educationId) {

        Education education = educationRepository
                .findByIdAndCvId(
                        educationId,
                        cvId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Formation introuvable avec l'id : "
                                + educationId));

        return new EducationResponse(education);
    }

    // ============================================================
    // UPDATE
    // ============================================================

    public EducationResponse update(
            Long cvId,
            Long educationId,
            EducationRequest request) {

        Education education = educationRepository
                .findByIdAndCvId(
                        educationId,
                        cvId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Formation introuvable avec l'id : "
                                + educationId));

        education.setInstitution(request.getInstitution());
        education.setDegree(request.getDegree());
        education.setFieldOfStudy(request.getFieldOfStudy());
        education.setLocation(request.getLocation());
        education.setStartDate(request.getStartDate());
        education.setEndDate(request.getEndDate());
        education.setCurrent(request.isCurrent());
        education.setDescription(request.getDescription());

        Education updated = educationRepository.save(education);

        return new EducationResponse(updated);
    }

    // ============================================================
    // DELETE
    // ============================================================

    public void delete(
            Long cvId,
            Long educationId) {

        Education education = educationRepository
                .findByIdAndCvId(
                        educationId,
                        cvId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Formation introuvable avec l'id : "
                                + educationId));

        educationRepository.delete(education);
    }
}