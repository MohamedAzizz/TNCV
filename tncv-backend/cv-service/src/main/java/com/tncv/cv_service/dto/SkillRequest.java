package com.tncv.cv_service.dto;

import jakarta.validation.constraints.NotBlank;

public class SkillRequest {

    @NotBlank(message = "Le nom de la compétence est obligatoire")
    private String name;

    private String category;

    private String level;

    private String description;

    public SkillRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}