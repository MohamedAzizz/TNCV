package com.tncv.cv_service.repository;

import com.tncv.cv_service.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    List<Skill> findByCvId(Long cvId);

    Optional<Skill> findByIdAndCvId(Long id, Long cvId);
}