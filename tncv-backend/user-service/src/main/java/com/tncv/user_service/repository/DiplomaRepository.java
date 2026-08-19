package com.tncv.user_service.repository;

import com.tncv.user_service.entity.Diploma;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiplomaRepository extends JpaRepository<Diploma, Long> {

    List<Diploma> findByUserId(Long userId);
}

