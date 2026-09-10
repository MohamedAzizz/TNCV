package com.tncv.cv_service.dto;

import com.tncv.cv_service.entity.Interest;

public class InterestResponse {

    private Long id;
    private String name;
    private String category;
    private String description;

    public InterestResponse(Interest interest) {
        this.id = interest.getId();
        this.name = interest.getName();
        this.category = interest.getCategory();
        this.description = interest.getDescription();
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

    public String getDescription() {
        return description;
    }
}