package com.tncv.cv_service.service;

import com.tncv.cv_service.dto.CvDownload;
import com.tncv.cv_service.dto.CvFileResponse;
import com.tncv.cv_service.entity.Cv;
import com.tncv.cv_service.exception.FileTooLargeException;
import com.tncv.cv_service.exception.InvalidFileTypeException;
import com.tncv.cv_service.exception.UnauthorizedFileAccessException;
import com.tncv.cv_service.repository.CvRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CvServiceTest {

    @Mock
    private CvRepository cvRepository;

    @Mock
    private MinioService minioService;

    @InjectMocks
    private CvService cvService;

    private Cv mockCv;
    private final String USER_ID = "kc-user-alice";
    private final String OTHER_USER_ID = "kc-user-bob";

    // Valid PDF header (%PDF = 0x25, 0x50, 0x44, 0x46)
    private final byte[] VALID_PDF_BYTES = new byte[]{0x25, 0x50, 0x44, 0x46, 0x2D, 0x31, 0x2E, 0x35};

    // Valid DOCX header (PK\x03\x04 = 0x50, 0x4B, 0x03, 0x04)
    private final byte[] VALID_DOCX_BYTES = new byte[]{0x50, 0x4B, 0x03, 0x04, 0x14, 0x00, 0x06, 0x00};

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(cvService, "cvsBucket", "tncv-cvs");
        ReflectionTestUtils.setField(cvService, "maxFileSizeBytes", 10485760L); // 10 MB

        mockCv = new Cv();
        mockCv.setId(100L);
        mockCv.setUserId(USER_ID);
        mockCv.setTitle("CV Développeur Java");
    }

    // ============================================================
    // UPLOAD TESTS
    // ============================================================

    @Test
    void uploadCvFile_pdf_success() {
        MockMultipartFile pdfFile = new MockMultipartFile(
                "file",
                "mon-cv.pdf",
                "application/pdf",
                VALID_PDF_BYTES
        );

        when(cvRepository.findById(100L)).thenReturn(Optional.of(mockCv));
        when(cvRepository.save(any(Cv.class))).thenReturn(mockCv);

        CvFileResponse response = cvService.uploadCvFile(USER_ID, 100L, null, pdfFile);

        assertNotNull(response);
        assertEquals(100L, response.getCvId());
        assertEquals("mon-cv.pdf", response.getOriginalFileName());
        assertEquals("application/pdf", response.getFileType());
        verify(minioService, times(1)).uploadFile(
                eq("tncv-cvs"),
                anyString(),
                any(InputStream.class),
                eq("application/pdf"),
                eq((long) VALID_PDF_BYTES.length)
        );
        verify(cvRepository, times(1)).save(mockCv);
    }

    @Test
    void uploadCvFile_docx_success() {
        MockMultipartFile docxFile = new MockMultipartFile(
                "file",
                "mon-cv.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                VALID_DOCX_BYTES
        );

        when(cvRepository.findById(100L)).thenReturn(Optional.of(mockCv));
        when(cvRepository.save(any(Cv.class))).thenReturn(mockCv);

        CvFileResponse response = cvService.uploadCvFile(USER_ID, 100L, null, docxFile);

        assertNotNull(response);
        assertEquals(100L, response.getCvId());
        assertEquals("mon-cv.docx", response.getOriginalFileName());
        verify(minioService, times(1)).uploadFile(
                eq("tncv-cvs"),
                anyString(),
                any(InputStream.class),
                eq("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
                eq((long) VALID_DOCX_BYTES.length)
        );
    }

    @Test
    void uploadCvFile_rejectTxt() {
        MockMultipartFile txtFile = new MockMultipartFile(
                "file",
                "notes.txt",
                "text/plain",
                "Ceci est un fichier texte".getBytes()
        );

        assertThrows(InvalidFileTypeException.class, () ->
                cvService.uploadCvFile(USER_ID, 100L, null, txtFile)
        );

        verify(minioService, never()).uploadFile(anyString(), anyString(), any(), anyString(), anyLong());
    }

    @Test
    void uploadCvFile_rejectExe() {
        MockMultipartFile exeFile = new MockMultipartFile(
                "file",
                "trojan.exe",
                "application/x-msdownload",
                new byte[]{0x4D, 0x5A, 0x00, 0x00}
        );

        assertThrows(InvalidFileTypeException.class, () ->
                cvService.uploadCvFile(USER_ID, 100L, null, exeFile)
        );

        verify(minioService, never()).uploadFile(anyString(), anyString(), any(), anyString(), anyLong());
    }

    @Test
    void uploadCvFile_fakePdfExtension_failsMagicBytes() {
        // Filename says .pdf, MIME type says application/pdf, but bytes are plain text
        MockMultipartFile fakePdf = new MockMultipartFile(
                "file",
                "fake.pdf",
                "application/pdf",
                "NOT A REAL PDF CONTENT".getBytes()
        );

        assertThrows(InvalidFileTypeException.class, () ->
                cvService.uploadCvFile(USER_ID, 100L, null, fakePdf)
        );

        verify(minioService, never()).uploadFile(anyString(), anyString(), any(), anyString(), anyLong());
    }

    @Test
    void uploadCvFile_oversizedFile_throwsFileTooLargeException() {
        byte[] oversized = new byte[11 * 1024 * 1024]; // 11 MB > 10 MB limit
        MockMultipartFile largeFile = new MockMultipartFile(
                "file",
                "heavy.pdf",
                "application/pdf",
                oversized
        );

        assertThrows(FileTooLargeException.class, () ->
                cvService.uploadCvFile(USER_ID, 100L, null, largeFile)
        );

        verify(minioService, never()).uploadFile(anyString(), anyString(), any(), anyString(), anyLong());
    }

    // ============================================================
    // DOWNLOAD & OWNERSHIP TESTS
    // ============================================================

    @Test
    void downloadCvFile_success() {
        mockCv.setFileObjectName("users/" + USER_ID + "/cvs/100/uuid.pdf");
        mockCv.setOriginalFileName("mon-cv.pdf");
        mockCv.setFileType("application/pdf");
        mockCv.setFileSize((long) VALID_PDF_BYTES.length);

        when(cvRepository.findById(100L)).thenReturn(Optional.of(mockCv));
        when(minioService.downloadFile("tncv-cvs", mockCv.getFileObjectName()))
                .thenReturn(new ByteArrayInputStream(VALID_PDF_BYTES));

        CvDownload download = cvService.downloadCvFile(100L, USER_ID);

        assertNotNull(download);
        assertEquals("mon-cv.pdf", download.fileName());
        assertEquals("application/pdf", download.contentType());
    }

    @Test
    void downloadCvFile_unauthorized_throwsForbidden() {
        when(cvRepository.findById(100L)).thenReturn(Optional.of(mockCv));

        // Bob tries to download Alice's CV
        assertThrows(UnauthorizedFileAccessException.class, () ->
                cvService.downloadCvFile(100L, OTHER_USER_ID)
        );

        verify(minioService, never()).downloadFile(anyString(), anyString());
    }

    // ============================================================
    // DELETE TESTS
    // ============================================================

    @Test
    void deleteCvFile_success() {
        mockCv.setFileObjectName("users/" + USER_ID + "/cvs/100/file.pdf");
        mockCv.setOriginalFileName("original.pdf");

        when(cvRepository.findById(100L)).thenReturn(Optional.of(mockCv));

        cvService.deleteCvFile(100L, USER_ID);

        verify(minioService, times(1)).deleteFile("tncv-cvs", "users/" + USER_ID + "/cvs/100/file.pdf");
        assertNull(mockCv.getFileObjectName());
        assertNull(mockCv.getOriginalFileName());
        verify(cvRepository, times(1)).save(mockCv);
    }

    @Test
    void deleteCvFile_unauthorized_throwsForbidden() {
        when(cvRepository.findById(100L)).thenReturn(Optional.of(mockCv));

        assertThrows(UnauthorizedFileAccessException.class, () ->
                cvService.deleteCvFile(100L, OTHER_USER_ID)
        );

        verify(minioService, never()).deleteFile(anyString(), anyString());
    }

    @Test
    void deleteCv_alsoDeletesMinioFile() {
        mockCv.setFileObjectName("users/" + USER_ID + "/cvs/100/file.pdf");
        when(cvRepository.findById(100L)).thenReturn(Optional.of(mockCv));

        cvService.deleteCv(100L, USER_ID);

        verify(minioService, times(1)).deleteFile("tncv-cvs", "users/" + USER_ID + "/cvs/100/file.pdf");
        verify(cvRepository, times(1)).delete(mockCv);
    }
}
