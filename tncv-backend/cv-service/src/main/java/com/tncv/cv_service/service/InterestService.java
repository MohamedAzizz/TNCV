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

    // ============================================================
    // CREATE
    // ============================================================

    public InterestResponse create(
            Long cvId,
            InterestRequest request) {

        Cv cv = cvRepository
                .findById(cvId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CV introuvable avec l'id : " + cvId));

        Interest interest = new Interest();

        interest.setName(request.getName());
        interest.setCategory(request.getCategory());
        interest.setDescription(request.getDescription());

        interest.setCv(cv);

        Interest savedInterest = interestRepository.save(interest);

        return new InterestResponse(savedInterest);
    }

    // ============================================================
    // GET ALL
    // ============================================================

    public List<InterestResponse> getByCv(Long cvId) {

        return interestRepository
                .findByCvId(cvId)
                .stream()
                .map(InterestResponse::new)
                .toList();
    }

    // ============================================================
    // GET ONE
    // ============================================================

    public InterestResponse get(
            Long cvId,
            Long interestId) {

        Interest interest = interestRepository
                .findByIdAndCvId(
                        interestId,
                        cvId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Centre d'intérêt introuvable avec l'id : "
                                + interestId));

        return new InterestResponse(interest);
    }

    // ============================================================
    // UPDATE
    // ============================================================

    public InterestResponse update(
            Long cvId,
            Long interestId,
            InterestRequest request) {

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

        Interest updatedInterest = interestRepository.save(interest);

        return new InterestResponse(updatedInterest);
    }

    // ============================================================
    // DELETE
    // ============================================================

    public void delete(
            Long cvId,
            Long interestId) {

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