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

        public EducationService(
                        EducationRepository educationRepository,
                        CvRepository cvRepository) {

                this.educationRepository = educationRepository;
                this.cvRepository = cvRepository;
        }

        private Cv getOwnedCv(Long cvId, String userId) {

                return cvRepository
                                .findByIdAndUserId(cvId, userId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "CV introuvable avec l'id : " + cvId));
        }

        public EducationResponse create(
                        Long cvId,
                        String userId,
                        EducationRequest request) {

                Cv cv = getOwnedCv(cvId, userId);

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

                return new EducationResponse(
                                educationRepository.save(education));
        }

        public List<EducationResponse> getByCv(
                        Long cvId,
                        String userId) {

                getOwnedCv(cvId, userId);

                return educationRepository
                                .findByCvId(cvId)
                                .stream()
                                .map(EducationResponse::new)
                                .toList();
        }

        public EducationResponse get(
                        Long cvId,
                        Long educationId,
                        String userId) {

                getOwnedCv(cvId, userId);

                Education education = educationRepository
                                .findByIdAndCvId(
                                                educationId,
                                                cvId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Formation introuvable avec l'id : "
                                                                + educationId));

                return new EducationResponse(education);
        }

        public EducationResponse update(
                        Long cvId,
                        Long educationId,
                        String userId,
                        EducationRequest request) {

                getOwnedCv(cvId, userId);

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

                return new EducationResponse(
                                educationRepository.save(education));
        }

        public void delete(
                        Long cvId,
                        Long educationId,
                        String userId) {

                getOwnedCv(cvId, userId);

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