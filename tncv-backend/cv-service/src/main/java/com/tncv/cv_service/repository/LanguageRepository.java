package com.tncv.cv_service.repository;

import com.tncv.cv_service.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LanguageRepository extends JpaRepository<Language, Long> {

    List<Language> findByCvId(Long cvId);

    Optional<Language> findByIdAndCvId(Long id, Long cvId);
}