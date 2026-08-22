package com.tncv.user_service.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ============================================================
    // VALIDATION
    // ============================================================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>>
    handleValidation(
            MethodArgumentNotValidException exception) {

        Map<String, String> errors =
                new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return ResponseEntity
                .badRequest()
                .body(errors);
    }

    // ============================================================
    // DATABASE
    // ============================================================

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>>
    handleDatabaseError(
            DataIntegrityViolationException exception) {

        Map<String, String> response =
                new HashMap<>();

        response.put(
                "message",
                "Erreur de contrainte PostgreSQL"
        );

        response.put(
                "details",
                exception.getMostSpecificCause()
                        .getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    // ============================================================
    // RUNTIME
    // ============================================================

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>>
    handleRuntimeException(
            RuntimeException exception) {

        Map<String, String> response =
                new HashMap<>();

        response.put(
                "message",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
}