package com.tncv.cv_service.repository;

import com.tncv.cv_service.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExperienceRepository
        extends JpaRepository<Experience, Long> {

    List<Experience> findByCvId(Long cvId);

    Optional<Experience> findByIdAndCvId(Long id, Long cvId);
}