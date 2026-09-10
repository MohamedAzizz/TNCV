package com.tncv.cv_service.dto;

import com.tncv.cv_service.entity.Skill;

public class SkillResponse {

    private Long id;
    private String name;
    private String category;
    private String level;
    private String description;

    public SkillResponse(Skill skill) {
        this.id = skill.getId();
        this.name = skill.getName();
        this.category = skill.getCategory();
        this.level = skill.getLevel();
        this.description = skill.getDescription();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }
}