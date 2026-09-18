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

        private Cv getOwnedCv(Long cvId, String userId) {

                return cvRepository
                                .findByIdAndUserId(cvId, userId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "CV introuvable avec l'id : " + cvId));
        }

        public LanguageResponse create(
                        Long cvId,
                        String userId,
                        LanguageRequest request) {

                Cv cv = getOwnedCv(cvId, userId);

                Language language = new Language();

                language.setName(request.getName());
                language.setLevel(request.getLevel());
                language.setCertification(request.getCertification());
                language.setDescription(request.getDescription());
                language.setCv(cv);

                return new LanguageResponse(
                                languageRepository.save(language));
        }

        public List<LanguageResponse> getByCv(
                        Long cvId,
                        String userId) {

                getOwnedCv(cvId, userId);

                return languageRepository
                                .findByCvId(cvId)
                                .stream()
                                .map(LanguageResponse::new)
                                .toList();
        }

        public LanguageResponse get(
                        Long cvId,
                        Long languageId,
                        String userId) {

                getOwnedCv(cvId, userId);

                Language language = languageRepository
                                .findByIdAndCvId(
                                                languageId,
                                                cvId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Langue introuvable avec l'id : "
                                                                + languageId));

                return new LanguageResponse(language);
        }

        public LanguageResponse update(
                        Long cvId,
                        Long languageId,
                        String userId,
                        LanguageRequest request) {

                getOwnedCv(cvId, userId);

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

                return new LanguageResponse(
                                languageRepository.save(language));
        }

        public void delete(
                        Long cvId,
                        Long languageId,
                        String userId) {

                getOwnedCv(cvId, userId);

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