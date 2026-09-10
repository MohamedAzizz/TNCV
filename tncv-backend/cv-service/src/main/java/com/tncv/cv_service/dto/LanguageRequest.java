package com.tncv.cv_service.dto;

import jakarta.validation.constraints.NotBlank;

public class LanguageRequest {

    @NotBlank(message = "Le nom de la langue est obligatoire")
    private String name;

    @NotBlank(message = "Le niveau de la langue est obligatoire")
    private String level;

    private String certification;

    private String description;

    public LanguageRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}