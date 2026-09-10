package com.tncv.cv_service.dto;

import com.tncv.cv_service.entity.Experience;

public class ExperienceResponse {

    private Long id;
    private String company;
    private String position;
    private String location;
    private String startDate;
    private String endDate;
    private boolean current;
    private String description;

    public ExperienceResponse(Experience experience) {

        this.id = experience.getId();
        this.company = experience.getCompany();
        this.position = experience.getPosition();
        this.location = experience.getLocation();
        this.startDate = experience.getStartDate();
        this.endDate = experience.getEndDate();
        this.current = experience.isCurrent();
        this.description = experience.getDescription();
    }

    public Long getId() {
        return id;
    }

    public String getCompany() {
        return company;
    }

    public String getPosition() {
        return position;
    }

    public String getLocation() {
        return location;
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

    public String getDescription() {
        return description;
    }
}