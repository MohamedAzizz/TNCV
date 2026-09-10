package com.tncv.cv_service.dto;

import com.tncv.cv_service.entity.Language;

public class LanguageResponse {

    private Long id;
    private String name;
    private String level;
    private String certification;
    private String description;

    public LanguageResponse(Language language) {
        this.id = language.getId();
        this.name = language.getName();
        this.level = language.getLevel();
        this.certification = language.getCertification();
        this.description = language.getDescription();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLevel() {
        return level;
    }

    public String getCertification() {
        return certification;
    }

    public String getDescription() {
        return description;
    }
}