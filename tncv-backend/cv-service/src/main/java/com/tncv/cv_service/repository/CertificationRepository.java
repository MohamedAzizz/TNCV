package com.tncv.cv_service.repository;

import com.tncv.cv_service.entity.Certification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CertificationRepository
        extends JpaRepository<Certification, Long> {

    List<Certification> findByCvId(Long cvId);

    Optional<Certification> findByIdAndCvId(
            Long id,
            Long cvId);
}