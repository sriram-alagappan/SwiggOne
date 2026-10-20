package com.swiggone.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            ResourceNotFoundException exception) {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("time", LocalDateTime.now().toString());
        response.put("status", 404);
        response.put("error", exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationError(
            MethodArgumentNotValidException exception) {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 400);

        String message = exception.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        response.put("error", message);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(
            IllegalArgumentException exception) {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 400);
        response.put("error", exception.getMessage());

        return ResponseEntity.badRequest().body(response);
    }
}
