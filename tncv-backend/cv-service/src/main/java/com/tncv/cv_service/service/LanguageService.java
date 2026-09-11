package com.tncv.cv_service.service;

import com.tncv.cv_service.dto.LanguageRequest;
import com.tncv.cv_service.dto.LanguageResponse;
import com.tncv.cv_service.entity.Cv;
import com.tncv.cv_service.entity.Language;
import com.tncv.cv_service.exception.ResourceNotFoundException;
import com.tncv.cv_service.repository.CvRepository;
import com.tncv.cv_service.repository.LanguageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageService {

    private final LanguageRepository languageRepository;
    private final CvRepository cvRepository;

    public LanguageService(
            LanguageRepository languageRepository,
            CvRepository cvRepository) {

        this.languageRepository = languageRepository;
        this.cvRepository = cvRepository;
    }

    // ============================================================
    // CREATE
    // ============================================================

    public LanguageResponse create(
            Long cvId,
            LanguageRequest request) {

        Cv cv = cvRepository
                .findById(cvId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CV introuvable avec l'id : " + cvId));

        Language language = new Language();

        language.setName(request.getName());
        language.setLevel(request.getLevel());
        language.setCertification(request.getCertification());
        language.setDescription(request.getDescription());

        language.setCv(cv);

        Language savedLanguage = languageRepository.save(language);

        return new LanguageResponse(savedLanguage);
    }

    // ============================================================
    // GET ALL
    // ============================================================

    public List<LanguageResponse> getByCv(Long cvId) {

        return languageRepository
                .findByCvId(cvId)
                .stream()
                .map(LanguageResponse::new)
                .toList();
    }

    // ============================================================
    // GET ONE
    // ============================================================

    public LanguageResponse get(
            Long cvId,
            Long languageId) {

        Language language = languageRepository
                .findByIdAndCvId(
                        languageId,
                        cvId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Langue introuvable avec l'id : "
                                + languageId));

        return new LanguageResponse(language);
    }

    // ============================================================
    // UPDATE
    // ============================================================

    public LanguageResponse update(
            Long cvId,
            Long languageId,
            LanguageRequest request) {

        Language language = languageRepository
                .findByIdAndCvId(
                        languageId,
                        cvId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Langue introuvable avec l'id : "
                                + languageId));

        language.setName(request.getName());
        language.setLevel(request.getLevel());
        language.setCertification(request.getCertification());
        language.setDescription(request.getDescription());

        Language updatedLanguage = languageRepository.save(language);

        return new LanguageResponse(updatedLanguage);
    }

    // ============================================================
    // DELETE
    // ============================================================

    public void delete(
            Long cvId,
            Long languageId) {

        Language language = languageRepository
                .findByIdAndCvId(
                        languageId,
                        cvId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Langue introuvable avec l'id : "
                                + languageId));

        languageRepository.delete(language);
    }
}