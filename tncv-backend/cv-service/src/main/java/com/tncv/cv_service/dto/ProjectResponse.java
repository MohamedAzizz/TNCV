package com.tncv.cv_service.dto;

import com.tncv.cv_service.entity.Project;

public class ProjectResponse {

    private Long id;
    private String name;
    private String description;
    private String technologies;
    private String startDate;
    private String endDate;
    private boolean current;
    private String githubUrl;
    private String projectUrl;

    public ProjectResponse(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.description = project.getDescription();
        this.technologies = project.getTechnologies();
        this.startDate = project.getStartDate();
        this.endDate = project.getEndDate();
        this.current = project.isCurrent();
        this.githubUrl = project.getGithubUrl();
        this.projectUrl = project.getProjectUrl();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getTechnologies() {
        return technologies;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public boolean isCurrent() {
        return current;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public String getProjectUrl() {
        return projectUrl;
    }
}