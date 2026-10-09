package com.tncv.cv_service.service;

import com.tncv.cv_service.exception.StorageException;
import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * Service responsible for all MinIO operations in cv-service.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket.cvs}")
    private String cvsBucket;

    // ============================================================
    // BUCKET INITIALIZATION
    // ============================================================

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

    public void uploadFile(String bucketName, String objectName, InputStream inputStream,
                           String contentType, long size) {
        try {
            ensureBucketExists(bucketName);

            log.info("Uploading CV file to MinIO: bucket={}, object={}", bucketName, objectName);

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, size, -1)
                            .contentType(contentType)
                            .build()
            );

            log.info("MinIO CV upload successful: {}/{}", bucketName, objectName);
        } catch (Exception e) {
            log.error("Failed to upload CV to MinIO: {}/{}", bucketName, objectName, e);
            throw new StorageException("Échec de l'upload du CV vers MinIO", e);
        }
    }

    // ============================================================
    // DOWNLOAD
    // ============================================================

    public InputStream downloadFile(String bucketName, String objectName) {
        try {
            log.info("Downloading CV from MinIO: bucket={}, object={}", bucketName, objectName);
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (ErrorResponseException e) {
            if (e.errorResponse().code().equals("NoSuchKey")) {
                throw new StorageException("Fichier CV introuvable dans MinIO: " + objectName);
            }
            throw new StorageException("Erreur lors du téléchargement du CV depuis MinIO", e);
        } catch (Exception e) {
            log.error("Failed to download CV from MinIO: {}/{}", bucketName, objectName, e);
            throw new StorageException("Erreur lors du téléchargement du CV depuis MinIO", e);
        }
    }

    // ============================================================
    // DELETE
    // ============================================================

    public void deleteFile(String bucketName, String objectName) {
        try {
            log.info("Deleting CV from MinIO: bucket={}, object={}", bucketName, objectName);
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            log.info("MinIO CV delete successful: {}/{}", bucketName, objectName);
        } catch (Exception e) {
            log.error("Failed to delete CV from MinIO: {}/{}", bucketName, objectName, e);
            throw new StorageException("Erreur lors de la suppression du CV depuis MinIO", e);
        }
    }

    // ============================================================
    // EXISTS
    // ============================================================

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
            throw new StorageException("Erreur lors de la vérification de l'objet MinIO", e);
        }
    }

    // ============================================================
    // PRESIGNED URL
    // ============================================================

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
            log.error("Failed to generate presigned URL for CV: {}/{}", bucketName, objectName, e);
            throw new StorageException("Erreur lors de la génération de l'URL présignée", e);
        }
    }
}
