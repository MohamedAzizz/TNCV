package com.tncv.cv_service.service;

import com.tncv.cv_service.dto.ExperienceRequest;
import com.tncv.cv_service.dto.ExperienceResponse;
import com.tncv.cv_service.entity.Cv;
import com.tncv.cv_service.entity.Experience;
import com.tncv.cv_service.exception.ResourceNotFoundException;
import com.tncv.cv_service.repository.CvRepository;
import com.tncv.cv_service.repository.ExperienceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final CvRepository cvRepository;

    public ExperienceService(
            ExperienceRepository experienceRepository,
            CvRepository cvRepository) {

        this.experienceRepository = experienceRepository;
        this.cvRepository = cvRepository;
    }

    // ============================================================
    // CREATE
    // ============================================================

    public ExperienceResponse create(
            Long cvId,
            ExperienceRequest request) {

        Cv cv = cvRepository
                .findById(cvId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CV introuvable avec l'id : " + cvId));

        Experience experience = new Experience();

        experience.setCompany(request.getCompany());
        experience.setPosition(request.getPosition());
        experience.setLocation(request.getLocation());
        experience.setStartDate(request.getStartDate());
        experience.setEndDate(request.getEndDate());
        experience.setCurrent(request.isCurrent());
        experience.setDescription(request.getDescription());

        experience.setCv(cv);

        Experience saved = experienceRepository.save(experience);

        return new ExperienceResponse(saved);
    }

    // ============================================================
    // GET ALL
    // ============================================================

    public List<ExperienceResponse> getByCv(Long cvId) {

        return experienceRepository
                .findByCvId(cvId)
                .stream()
                .map(ExperienceResponse::new)
                .toList();
    }

    // ============================================================
    // GET ONE
    // ============================================================

    public ExperienceResponse get(
            Long cvId,
            Long experienceId) {

        Experience experience = experienceRepository
                .findByIdAndCvId(
                        experienceId,
                        cvId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Expérience introuvable avec l'id : "
                                + experienceId));

        return new ExperienceResponse(experience);
    }

    // ============================================================
    // UPDATE
    // ============================================================

    public ExperienceResponse update(
            Long cvId,
            Long experienceId,
            ExperienceRequest request) {

        Experience experience = experienceRepository
                .findByIdAndCvId(
                        experienceId,
                        cvId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Expérience introuvable avec l'id : "
                                + experienceId));

        experience.setCompany(request.getCompany());
        experience.setPosition(request.getPosition());
        experience.setLocation(request.getLocation());
        experience.setStartDate(request.getStartDate());
        experience.setEndDate(request.getEndDate());
        experience.setCurrent(request.isCurrent());
        experience.setDescription(request.getDescription());

        Experience updated = experienceRepository.save(experience);

        return new ExperienceResponse(updated);
    }

    // ============================================================
    // DELETE
    // ============================================================

    public void delete(
            Long cvId,
            Long experienceId) {

        Experience experience = experienceRepository
                .findByIdAndCvId(
                        experienceId,
                        cvId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Expérience introuvable avec l'id : "
                                + experienceId));

        experienceRepository.delete(experience);
    }
}