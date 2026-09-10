package com.tncv.cv_service.service;

import com.tncv.cv_service.dto.ProjectRequest;
import com.tncv.cv_service.dto.ProjectResponse;
import com.tncv.cv_service.entity.Cv;
import com.tncv.cv_service.entity.Project;
import com.tncv.cv_service.repository.CvRepository;
import com.tncv.cv_service.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final CvRepository cvRepository;

    public ProjectService(
            ProjectRepository projectRepository,
            CvRepository cvRepository) {
        this.projectRepository = projectRepository;
        this.cvRepository = cvRepository;
    }

    // CREATE
    public ProjectResponse create(
            Long cvId,
            ProjectRequest request) {

        Cv cv = cvRepository.findById(cvId)
                .orElseThrow(() -> new RuntimeException(
                        "CV introuvable avec l'id : " + cvId));

        Project project = new Project();

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setTechnologies(request.getTechnologies());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        project.setCurrent(request.isCurrent());
        project.setGithubUrl(request.getGithubUrl());
        project.setProjectUrl(request.getProjectUrl());

        project.setCv(cv);

        Project savedProject = projectRepository.save(project);

        return new ProjectResponse(savedProject);
    }

    // GET ALL
    public List<ProjectResponse> getByCv(Long cvId) {

        return projectRepository.findByCvId(cvId)
                .stream()
                .map(ProjectResponse::new)
                .toList();
    }

    // GET ONE
    public ProjectResponse get(
            Long cvId,
            Long projectId) {

        Project project = projectRepository
                .findByIdAndCvId(projectId, cvId)
                .orElseThrow(() -> new RuntimeException(
                        "Projet introuvable avec l'id : "
                                + projectId));

        return new ProjectResponse(project);
    }

    // UPDATE
    public ProjectResponse update(
            Long cvId,
            Long projectId,
            ProjectRequest request) {

        Project project = projectRepository
                .findByIdAndCvId(projectId, cvId)
                .orElseThrow(() -> new RuntimeException(
                        "Projet introuvable avec l'id : "
                                + projectId));

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setTechnologies(request.getTechnologies());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        project.setCurrent(request.isCurrent());
        project.setGithubUrl(request.getGithubUrl());
        project.setProjectUrl(request.getProjectUrl());

        Project updatedProject = projectRepository.save(project);

        return new ProjectResponse(updatedProject);
    }

    // DELETE
    public void delete(
            Long cvId,
            Long projectId) {

        Project project = projectRepository
                .findByIdAndCvId(projectId, cvId)
                .orElseThrow(() -> new RuntimeException(
                        "Projet introuvable avec l'id : "
                                + projectId));

        projectRepository.delete(project);
    }
}