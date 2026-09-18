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

        private Cv getOwnedCv(Long cvId, String userId) {

                return cvRepository
                                .findByIdAndUserId(cvId, userId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "CV introuvable avec l'id : " + cvId));
        }

        public ExperienceResponse create(
                        Long cvId,
                        String userId,
                        ExperienceRequest request) {

                Cv cv = getOwnedCv(cvId, userId);

                Experience experience = new Experience();

                experience.setCompany(request.getCompany());
                experience.setPosition(request.getPosition());
                experience.setLocation(request.getLocation());
                experience.setStartDate(request.getStartDate());
                experience.setEndDate(request.getEndDate());
                experience.setCurrent(request.isCurrent());
                experience.setDescription(request.getDescription());
                experience.setCv(cv);

                return new ExperienceResponse(
                                experienceRepository.save(experience));
        }

        public List<ExperienceResponse> getByCv(
                        Long cvId,
                        String userId) {

                getOwnedCv(cvId, userId);

                return experienceRepository
                                .findByCvId(cvId)
                                .stream()
                                .map(ExperienceResponse::new)
                                .toList();
        }

        public ExperienceResponse get(
                        Long cvId,
                        Long experienceId,
                        String userId) {

                getOwnedCv(cvId, userId);

                Experience experience = experienceRepository
                                .findByIdAndCvId(
                                                experienceId,
                                                cvId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Expérience introuvable avec l'id : "
                                                                + experienceId));

                return new ExperienceResponse(experience);
        }

        public ExperienceResponse update(
                        Long cvId,
                        Long experienceId,
                        String userId,
                        ExperienceRequest request) {

                getOwnedCv(cvId, userId);

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

                return new ExperienceResponse(
                                experienceRepository.save(experience));
        }

        public void delete(
                        Long cvId,
                        Long experienceId,
                        String userId) {

                getOwnedCv(cvId, userId);

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