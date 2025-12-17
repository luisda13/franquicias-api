package com.franquicias.franquicias_api.domain.exception;

public class ExceptionFranquicia extends RuntimeException {
    public ExceptionFranquicia(String message) {
        super("La franquicia con nombre '" + message + "' ya existe en el sistema.");
    }
}
