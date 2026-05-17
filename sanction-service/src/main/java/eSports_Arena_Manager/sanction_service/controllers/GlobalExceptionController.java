package eSports_Arena_Manager.sanction_service.controllers;

import eSports_Arena_Manager.sanction_service.exceptions.SanctionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(SanctionException.class)
    public ResponseEntity<?> handleSanctionException(SanctionException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        // Devolvemos un 400 (Bad Request) en lugar de un 500
        return ResponseEntity.badRequest().body(error);
    }
}