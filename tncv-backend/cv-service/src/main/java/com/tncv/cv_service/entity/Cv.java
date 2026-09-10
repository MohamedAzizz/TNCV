package com.tncv.cv_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cvs")
public class Cv {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String title;

    private String fullName;

    private String email;

    private String phone;

    private String address;

    private String linkedin;

    private String github;

    @Column(columnDefinition = "TEXT")
    private String summary;

    public Cv() {
    }

    public Cv(
            Long id,
            String userId,
            String title,
            String fullName,
            String email,
            String phone,
            String address,
            String linkedin,
            String github,
            String summary) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.linkedin = linkedin;
        this.github = github;
        this.summary = summary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}