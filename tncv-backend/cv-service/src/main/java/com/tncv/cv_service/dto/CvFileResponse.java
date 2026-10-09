package com.tncv.cv_service.dto;

public class CvFileResponse {

    private Long cvId;
    private String originalFileName;
    private String fileType;
    private Long fileSize;
    private String downloadUrl;
    private String message;

    public CvFileResponse() {}

    public CvFileResponse(Long cvId, String originalFileName, String fileType, Long fileSize, String downloadUrl, String message) {
        this.cvId = cvId;
        this.originalFileName = originalFileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.downloadUrl = downloadUrl;
        this.message = message;
    }

    public static CvFileResponse of(Long cvId, String originalFileName, String fileType, Long fileSize, String message) {
        return new CvFileResponse(cvId, originalFileName, fileType, fileSize, "/api/cvs/" + cvId + "/file", message);
    }

    public Long getCvId() { return cvId; }
    public void setCvId(Long cvId) { this.cvId = cvId; }
    public String getOriginalFileName() { return originalFileName; }
    public void setOriginalFileName(String originalFileName) { this.originalFileName = originalFileName; }
    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
    public String getDownloadUrl() { return downloadUrl; }
    public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
