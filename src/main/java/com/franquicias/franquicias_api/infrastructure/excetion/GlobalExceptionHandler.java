package com.franquicias.franquicias_api.infrastructure.excetion;

import com.franquicias.franquicias_api.domain.exception.RecursoNoEncontradoException;
import com.franquicias.franquicias_api.domain.exception.ExceptionFranquicia; // Tu excepción de conflicto
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;
import java.time.Instant;
import java.util.Map;

// Indica que esta clase maneja excepciones de todos los controladores REST
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja RecursoNoEncontradoException -> HTTP 404 Not Found
     * Ocurre en findById, findByNombre.
     */
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleNotFoundException(RecursoNoEncontradoException ex) {
        return Mono.just(
                ResponseEntity
                        .status(HttpStatus.NOT_FOUND) // Código 404
                        .body(this.buildErrorBody(HttpStatus.NOT_FOUND, ex.getMessage()))
        );
    }

    /**
     * Maneja ExceptionFranquicia -> HTTP 409 Conflict
     * Ocurre en crearFranquicia cuando ya existe.
     */
    @ExceptionHandler(ExceptionFranquicia.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleConflictException(ExceptionFranquicia ex) {
        return Mono.just(
                ResponseEntity
                        .status(HttpStatus.CONFLICT) // Código 409
                        .body(this.buildErrorBody(HttpStatus.CONFLICT, ex.getMessage()))
        );
    }

    /**
     * Maneja IllegalArgumentException -> HTTP 400 Bad Request
     * Ocurre por datos de entrada faltantes o incorrectos (validaciones de campos vacíos).
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleBadRequest(IllegalArgumentException ex) {
        return Mono.just(
                ResponseEntity
                        .status(HttpStatus.BAD_REQUEST) // Código 400
                        .body(this.buildErrorBody(HttpStatus.BAD_REQUEST, ex.getMessage()))
        );
    }

    // Metodo auxiliar para generar un cuerpo de error JSON estándar
    private Map<String, Object> buildErrorBody(HttpStatus status, String message) {
        return Map.of(
                "timestamp", Instant.now().toString(),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", message
        );
    }
}