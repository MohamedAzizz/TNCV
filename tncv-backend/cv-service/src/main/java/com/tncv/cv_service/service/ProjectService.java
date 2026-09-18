package com.tncv.cv_service.service;

import com.tncv.cv_service.dto.ProjectRequest;
import com.tncv.cv_service.dto.ProjectResponse;
import com.tncv.cv_service.entity.Cv;
import com.tncv.cv_service.entity.Project;
import com.tncv.cv_service.exception.ResourceNotFoundException;
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

        private Cv getOwnedCv(Long cvId, String userId) {

                return cvRepository
                                .findByIdAndUserId(cvId, userId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "CV introuvable avec l'id : " + cvId));
        }

        public ProjectResponse create(
                        Long cvId,
                        String userId,
                        ProjectRequest request) {

                Cv cv = getOwnedCv(cvId, userId);

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

                return new ProjectResponse(
                                projectRepository.save(project));
        }

        public List<ProjectResponse> getByCv(
                        Long cvId,
                        String userId) {

                getOwnedCv(cvId, userId);

                return projectRepository
                                .findByCvId(cvId)
                                .stream()
                                .map(ProjectResponse::new)
                                .toList();
        }

        public ProjectResponse get(
                        Long cvId,
                        Long projectId,
                        String userId) {

                getOwnedCv(cvId, userId);

                Project project = projectRepository
                                .findByIdAndCvId(
                                                projectId,
                                                cvId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Projet introuvable avec l'id : "
                                                                + projectId));

                return new ProjectResponse(project);
        }

        public ProjectResponse update(
                        Long cvId,
                        Long projectId,
                        String userId,
                        ProjectRequest request) {

                getOwnedCv(cvId, userId);

                Project project = projectRepository
                                .findByIdAndCvId(
                                                projectId,
                                                cvId)
                                .orElseThrow(() -> new ResourceNotFoundException(
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

                return new ProjectResponse(
                                projectRepository.save(project));
        }

        public void delete(
                        Long cvId,
                        Long projectId,
                        String userId) {

                getOwnedCv(cvId, userId);

                Project project = projectRepository
                                .findByIdAndCvId(
                                                projectId,
                                                cvId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Projet introuvable avec l'id : "
                                                                + projectId));

                projectRepository.delete(project);
        }
}