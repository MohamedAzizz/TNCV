package com.tncv.cv_service.service;

import com.tncv.cv_service.dto.ExperienceRequest;
import com.tncv.cv_service.dto.ExperienceResponse;
import com.tncv.cv_service.entity.Cv;
import com.tncv.cv_service.entity.Experience;
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

    public ExperienceResponse create(
            Long cvId,
            ExperienceRequest request) {

        Cv cv = cvRepository.findById(cvId)
                .orElseThrow(() -> new RuntimeException("CV introuvable"));

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

    public List<ExperienceResponse> getByCv(Long cvId) {

        return experienceRepository
                .findByCvId(cvId)
                .stream()
                .map(ExperienceResponse::new)
                .toList();
    }

    public ExperienceResponse get(
            Long cvId,
            Long experienceId) {

        Experience experience = experienceRepository
                .findByIdAndCvId(
                        experienceId,
                        cvId)
                .orElseThrow(() -> new RuntimeException(
                        "Expérience introuvable"));

        return new ExperienceResponse(experience);
    }

    public ExperienceResponse update(
            Long cvId,
            Long experienceId,
            ExperienceRequest request) {

        Experience experience = experienceRepository
                .findByIdAndCvId(
                        experienceId,
                        cvId)
                .orElseThrow(() -> new RuntimeException(
                        "Expérience introuvable"));

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

    public void delete(
            Long cvId,
            Long experienceId) {

        Experience experience = experienceRepository
                .findByIdAndCvId(
                        experienceId,
                        cvId)
                .orElseThrow(() -> new RuntimeException(
                        "Expérience introuvable"));

        experienceRepository.delete(experience);
    }
}