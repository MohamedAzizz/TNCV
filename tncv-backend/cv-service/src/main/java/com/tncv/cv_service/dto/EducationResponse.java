package com.tncv.cv_service.dto;

import com.tncv.cv_service.entity.Education;

public class EducationResponse {

    private Long id;

    private String institution;

    private String degree;

    private String fieldOfStudy;

    private String location;

    private String startDate;

    private String endDate;

    private boolean current;

    private String description;

    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public EducationResponse(Education education) {

        this.id = education.getId();

        this.institution = education.getInstitution();

        this.degree = education.getDegree();

        this.fieldOfStudy = education.getFieldOfStudy();

        this.location = education.getLocation();

        this.startDate = education.getStartDate();

        this.endDate = education.getEndDate();

        this.current = education.isCurrent();

        this.description = education.getDescription();
    }

    // ============================================================
    // GETTERS
    // ============================================================

    public Long getId() {
        return id;
    }

    public String getInstitution() {
        return institution;
    }

    public String getDegree() {
        return degree;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
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