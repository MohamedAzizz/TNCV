package com.tncv.cv_service.repository;

import com.tncv.cv_service.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterestRepository
        extends JpaRepository<Interest, Long> {

    List<Interest> findByCvId(Long cvId);

    Optional<Interest> findByIdAndCvId(
            Long id,
            Long cvId);
}