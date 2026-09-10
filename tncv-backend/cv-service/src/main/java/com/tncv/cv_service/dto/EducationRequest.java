package com.tncv.cv_service.dto;

import jakarta.validation.constraints.NotBlank;

public class EducationRequest {

    @NotBlank(message = "L'établissement est obligatoire")
    private String institution;

    @NotBlank(message = "Le diplôme est obligatoire")
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

    public EducationRequest() {
    }

    // ============================================================
    // GETTERS / SETTERS
    // ============================================================

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}