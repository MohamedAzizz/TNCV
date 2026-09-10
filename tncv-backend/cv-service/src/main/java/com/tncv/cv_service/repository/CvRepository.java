package com.tncv.cv_service.repository;

import com.tncv.cv_service.entity.Cv;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CvRepository extends JpaRepository<Cv, Long> {

    List<Cv> findByUserId(String userId);

    Optional<Cv> findByIdAndUserId(Long id, String userId);
}