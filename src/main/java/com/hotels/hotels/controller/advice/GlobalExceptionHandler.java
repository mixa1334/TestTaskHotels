package com.hotels.hotels.controller.advice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleInvalidInput(ConstraintViolationException exception,
            HttpServletRequest request) {
        List<String> messages = exception
                .getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .toList();
        return ResponseEntity.badRequest().body(createResponse("Validation Failed", messages, request));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleInvalidInput(IllegalArgumentException exception, HttpServletRequest request) {
        List<String> messages = List.of(exception.getMessage());
        return ResponseEntity.badRequest().body(createResponse("Invalid input", messages, request));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleInvalidInput(DataIntegrityViolationException exception,
            HttpServletRequest request) {
        List<String> messages = List.of("Entity with same values already exists");
        return ResponseEntity.badRequest().body(createResponse("Invalid input", messages, request));
    }

    public Object createResponse(String error, List<String> messages, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", error);
        response.put("method", request.getMethod());
        response.put("path", request.getRequestURI());
        response.put("message", messages);
        return response;
    }
}
