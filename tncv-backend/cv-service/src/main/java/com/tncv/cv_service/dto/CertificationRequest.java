package com.tncv.cv_service.dto;

import jakarta.validation.constraints.NotBlank;

public class CertificationRequest {

    @NotBlank(message = "Le nom de la certification est obligatoire")
    private String name;

    private String issuingOrganization;

    private String issueDate;

    private String expirationDate;

    private boolean noExpiration;

    private String credentialId;

    private String credentialUrl;

    private String description;

    public CertificationRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIssuingOrganization() {
        return issuingOrganization;
    }

    public void setIssuingOrganization(String issuingOrganization) {
        this.issuingOrganization = issuingOrganization;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isNoExpiration() {
        return noExpiration;
    }

    public void setNoExpiration(boolean noExpiration) {
        this.noExpiration = noExpiration;
    }

    public String getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
    }

    public String getCredentialUrl() {
        return credentialUrl;
    }

    public void setCredentialUrl(String credentialUrl) {
        this.credentialUrl = credentialUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}