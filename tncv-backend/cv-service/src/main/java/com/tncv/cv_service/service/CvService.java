package com.tncv.cv_service.service;

import com.tncv.cv_service.dto.CvRequest;
import com.tncv.cv_service.dto.CvResponse;
import com.tncv.cv_service.entity.Cv;
import com.tncv.cv_service.repository.CvRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CvService {

    private final CvRepository cvRepository;

    public CvService(CvRepository cvRepository) {
        this.cvRepository = cvRepository;
    }

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

        return new CvResponse(savedCv);
    }

    public List<CvResponse> getUserCvs(String userId) {

        return cvRepository.findByUserId(userId)
                .stream()
                .map(CvResponse::new)
                .toList();
    }

    public CvResponse getCv(Long id, String userId) {

        Cv cv = cvRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("CV introuvable"));

        return new CvResponse(cv);
    }

    public CvResponse updateCv(
            Long id,
            String userId,
            CvRequest request) {

        Cv cv = cvRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("CV introuvable"));

        cv.setTitle(request.getTitle());
        cv.setFullName(request.getFullName());
        cv.setEmail(request.getEmail());
        cv.setPhone(request.getPhone());
        cv.setAddress(request.getAddress());
        cv.setLinkedin(request.getLinkedin());
        cv.setGithub(request.getGithub());
        cv.setSummary(request.getSummary());

        Cv updatedCv = cvRepository.save(cv);

        return new CvResponse(updatedCv);
    }

    public void deleteCv(Long id, String userId) {

        Cv cv = cvRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("CV introuvable"));

        cvRepository.delete(cv);
    }
}