package com.tncv.cv_service.service;

import com.tncv.cv_service.dto.SkillRequest;
import com.tncv.cv_service.dto.SkillResponse;
import com.tncv.cv_service.entity.Cv;
import com.tncv.cv_service.entity.Skill;
import com.tncv.cv_service.exception.ResourceNotFoundException;
import com.tncv.cv_service.repository.CvRepository;
import com.tncv.cv_service.repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {

    private final SkillRepository skillRepository;
    private final CvRepository cvRepository;

    public SkillService(
            SkillRepository skillRepository,
            CvRepository cvRepository) {

        this.skillRepository = skillRepository;
        this.cvRepository = cvRepository;
    }

    private Cv getOwnedCv(
            Long cvId,
            String userId) {

        return cvRepository
                .findByIdAndUserId(cvId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CV introuvable avec l'id : " + cvId));
    }

    // ============================================================
    // CREATE
    // ============================================================

    public SkillResponse create(
            Long cvId,
            String userId,
            SkillRequest request) {

        Cv cv = getOwnedCv(cvId, userId);

        Skill skill = new Skill();

        skill.setName(request.getName());
        skill.setCategory(request.getCategory());
        skill.setLevel(request.getLevel());
        skill.setDescription(request.getDescription());
        skill.setCv(cv);

        Skill savedSkill = skillRepository.save(skill);

        return new SkillResponse(savedSkill);
    }

    // ============================================================
    // GET ALL
    // ============================================================

    public List<SkillResponse> getByCv(
            Long cvId,
            String userId) {

        getOwnedCv(cvId, userId);

        return skillRepository
                .findByCvId(cvId)
                .stream()
                .map(SkillResponse::new)
                .toList();
    }

    // ============================================================
    // GET ONE
    // ============================================================

    public SkillResponse get(
            Long cvId,
            Long skillId,
            String userId) {

        getOwnedCv(cvId, userId);

        Skill skill = skillRepository
                .findByIdAndCvId(
                        skillId,
                        cvId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Compétence introuvable avec l'id : "
                                + skillId));

        return new SkillResponse(skill);
    }

    // ============================================================
    // UPDATE
    // ============================================================

    public SkillResponse update(
            Long cvId,
            Long skillId,
            String userId,
            SkillRequest request) {

        getOwnedCv(cvId, userId);

        Skill skill = skillRepository
                .findByIdAndCvId(
                        skillId,
                        cvId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Compétence introuvable avec l'id : "
                                + skillId));

        skill.setName(request.getName());
        skill.setCategory(request.getCategory());
        skill.setLevel(request.getLevel());
        skill.setDescription(request.getDescription());

        Skill updatedSkill = skillRepository.save(skill);

        return new SkillResponse(updatedSkill);
    }

    // ============================================================
    // DELETE
    // ============================================================

    public void delete(
            Long cvId,
            Long skillId,
            String userId) {

        getOwnedCv(cvId, userId);

        Skill skill = skillRepository
                .findByIdAndCvId(
                        skillId,
                        cvId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Compétence introuvable avec l'id : "
                                + skillId));

        skillRepository.delete(skill);
    }
}