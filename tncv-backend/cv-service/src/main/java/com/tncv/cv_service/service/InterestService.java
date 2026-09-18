package com.tncv.cv_service.service;

import com.tncv.cv_service.dto.InterestRequest;
import com.tncv.cv_service.dto.InterestResponse;
import com.tncv.cv_service.entity.Cv;
import com.tncv.cv_service.entity.Interest;
import com.tncv.cv_service.exception.ResourceNotFoundException;
import com.tncv.cv_service.repository.CvRepository;
import com.tncv.cv_service.repository.InterestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterestService {

        private final InterestRepository interestRepository;
        private final CvRepository cvRepository;

        public InterestService(
                        InterestRepository interestRepository,
                        CvRepository cvRepository) {

                this.interestRepository = interestRepository;
                this.cvRepository = cvRepository;
        }

        private Cv getOwnedCv(Long cvId, String userId) {

                return cvRepository
                                .findByIdAndUserId(cvId, userId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "CV introuvable avec l'id : " + cvId));
        }

        public InterestResponse create(
                        Long cvId,
                        String userId,
                        InterestRequest request) {

                Cv cv = getOwnedCv(cvId, userId);

                Interest interest = new Interest();

                interest.setName(request.getName());
                interest.setCategory(request.getCategory());
                interest.setDescription(request.getDescription());
                interest.setCv(cv);

                return new InterestResponse(
                                interestRepository.save(interest));
        }

        public List<InterestResponse> getByCv(
                        Long cvId,
                        String userId) {

                getOwnedCv(cvId, userId);

                return interestRepository
                                .findByCvId(cvId)
                                .stream()
                                .map(InterestResponse::new)
                                .toList();
        }

        public InterestResponse get(
                        Long cvId,
                        Long interestId,
                        String userId) {

                getOwnedCv(cvId, userId);

                Interest interest = interestRepository
                                .findByIdAndCvId(
                                                interestId,
                                                cvId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Centre d'intérêt introuvable avec l'id : "
                                                                + interestId));

                return new InterestResponse(interest);
        }

        public InterestResponse update(
                        Long cvId,
                        Long interestId,
                        String userId,
                        InterestRequest request) {

                getOwnedCv(cvId, userId);

                Interest interest = interestRepository
                                .findByIdAndCvId(
                                                interestId,
                                                cvId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Centre d'intérêt introuvable avec l'id : "
                                                                + interestId));

                interest.setName(request.getName());
                interest.setCategory(request.getCategory());
                interest.setDescription(request.getDescription());

                return new InterestResponse(
                                interestRepository.save(interest));
        }

        public void delete(
                        Long cvId,
                        Long interestId,
                        String userId) {

                getOwnedCv(cvId, userId);

                Interest interest = interestRepository
                                .findByIdAndCvId(
                                                interestId,
                                                cvId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Centre d'intérêt introuvable avec l'id : "
                                                                + interestId));

                interestRepository.delete(interest);
        }
}