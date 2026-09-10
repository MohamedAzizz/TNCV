package com.tncv.cv_service.repository;

import com.tncv.cv_service.entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EducationRepository
        extends JpaRepository<Education, Long> {

    List<Education> findByCvId(Long cvId);

    Optional<Education> findByIdAndCvId(
            Long id,
            Long cvId);
}