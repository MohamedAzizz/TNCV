package com.tncv.cv_service.dto;

import com.tncv.cv_service.entity.Cv;

public class CvResponse {

    private Long id;
    private String userId;
    private String title;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String linkedin;
    private String github;
    private String summary;

    public CvResponse(Cv cv) {
        this.id = cv.getId();
        this.userId = cv.getUserId();
        this.title = cv.getTitle();
        this.fullName = cv.getFullName();
        this.email = cv.getEmail();
        this.phone = cv.getPhone();
        this.address = cv.getAddress();
        this.linkedin = cv.getLinkedin();
        this.github = cv.getGithub();
        this.summary = cv.getSummary();
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public String getGithub() {
        return github;
    }

    public String getSummary() {
        return summary;
    }
}