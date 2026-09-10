package com.tncv.cv_service.dto;

import com.tncv.cv_service.entity.Certification;

public class CertificationResponse {

    private Long id;
    private String name;
    private String issuingOrganization;
    private String issueDate;
    private String expirationDate;
    private boolean noExpiration;
    private String credentialId;
    private String credentialUrl;
    private String description;

    public CertificationResponse(Certification certification) {

        this.id = certification.getId();
        this.name = certification.getName();
        this.issuingOrganization = certification.getIssuingOrganization();
        this.issueDate = certification.getIssueDate();
        this.expirationDate = certification.getExpirationDate();
        this.noExpiration = certification.isNoExpiration();
        this.credentialId = certification.getCredentialId();
        this.credentialUrl = certification.getCredentialUrl();
        this.description = certification.getDescription();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIssuingOrganization() {
        return issuingOrganization;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public boolean isNoExpiration() {
        return noExpiration;
    }

    public String getCredentialId() {
        return credentialId;
    }

    public String getCredentialUrl() {
        return credentialUrl;
    }

    public String getDescription() {
        return description;
    }
}