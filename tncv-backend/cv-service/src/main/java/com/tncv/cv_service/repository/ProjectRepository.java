package com.tncv.cv_service.repository;

import com.tncv.cv_service.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByCvId(Long cvId);

    Optional<Project> findByIdAndCvId(Long id, Long cvId);
}