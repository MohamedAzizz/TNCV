package com.tncv.user_service.service;

import com.tncv.user_service.exception.StorageException;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * Service responsible for all MinIO operations.
 * Handles upload, download, delete, and presigned URL generation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket.profile-images}")
    private String profileImagesBucket;

    // ============================================================
    // BUCKET INITIALIZATION
    // ============================================================

    /**
     * Ensures a bucket exists; creates it if not.
     */
    public void ensureBucketExists(String bucketName) {
        try {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(bucketName)
                            .build()
            );
            if (!exists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucketName)
                                .build()
                );
                log.info("MinIO bucket created: {}", bucketName);
            }
        } catch (Exception e) {
            log.error("Failed to ensure bucket exists: {}", bucketName, e);
            throw new StorageException("Impossible de vérifier/créer le bucket MinIO: " + bucketName, e);
        }
    }

    // ============================================================
    // UPLOAD
    // ============================================================

    /**
     * Uploads a file to MinIO.
     *
     * @param bucketName  target bucket
     * @param objectName  full object path/name inside the bucket
     * @param inputStream file content
     * @param contentType MIME type
     * @param size        file size in bytes (-1 if unknown)
     */
    public void uploadFile(String bucketName, String objectName, InputStream inputStream,
                           String contentType, long size) {
        try {
            ensureBucketExists(bucketName);

            log.info("Uploading file to MinIO: bucket={}, object={}", bucketName, objectName);

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, size, -1)
                            .contentType(contentType)
                            .build()
            );

            log.info("MinIO upload successful: {}/{}", bucketName, objectName);
        } catch (Exception e) {
            log.error("Failed to upload file to MinIO: {}/{}", bucketName, objectName, e);
            throw new StorageException("Échec de l'upload vers MinIO", e);
        }
    }

    // ============================================================
    // DOWNLOAD
    // ============================================================

    /**
     * Downloads a file from MinIO.
     */
    public InputStream downloadFile(String bucketName, String objectName) {
        try {
            log.info("Downloading file from MinIO: bucket={}, object={}", bucketName, objectName);
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (ErrorResponseException e) {
            if (e.errorResponse().code().equals("NoSuchKey")) {
                throw new StorageException("Fichier introuvable dans MinIO: " + objectName);
            }
            throw new StorageException("Erreur lors du téléchargement depuis MinIO", e);
        } catch (Exception e) {
            log.error("Failed to download file from MinIO: {}/{}", bucketName, objectName, e);
            throw new StorageException("Erreur lors du téléchargement depuis MinIO", e);
        }
    }

    // ============================================================
    // DELETE
    // ============================================================

    /**
     * Deletes a file from MinIO.
     */
    public void deleteFile(String bucketName, String objectName) {
        try {
            log.info("Deleting file from MinIO: bucket={}, object={}", bucketName, objectName);
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            log.info("MinIO delete successful: {}/{}", bucketName, objectName);
        } catch (Exception e) {
            log.error("Failed to delete file from MinIO: {}/{}", bucketName, objectName, e);
            throw new StorageException("Erreur lors de la suppression depuis MinIO", e);
        }
    }

    // ============================================================
    // EXISTS
    // ============================================================

    /**
     * Checks if an object exists in MinIO.
     */
    public boolean fileExists(String bucketName, String objectName) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            return true;
        } catch (ErrorResponseException e) {
            if (e.errorResponse().code().equals("NoSuchKey")) {
                return false;
            }
            throw new StorageException("Erreur lors de la vérification de l'objet MinIO", e);
        } catch (Exception e) {
            log.error("Error checking existence of MinIO object: {}/{}", bucketName, objectName, e);
            throw new StorageException("Erreur lors de la vérification de l'objet MinIO", e);
        }
    }

    // ============================================================
    // PRESIGNED URL
    // ============================================================

    /**
     * Generates a presigned URL for temporary read access.
     *
     * @param bucketName    target bucket
     * @param objectName    object path/name
     * @param expirySeconds expiry time in seconds
     * @return presigned URL string
     */
    public String generatePresignedUrl(String bucketName, String objectName, int expirySeconds) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(expirySeconds, TimeUnit.SECONDS)
                            .build()
            );
        } catch (Exception e) {
            log.error("Failed to generate presigned URL for: {}/{}", bucketName, objectName, e);
            throw new StorageException("Erreur lors de la génération de l'URL présignée", e);
        }
    }
}
