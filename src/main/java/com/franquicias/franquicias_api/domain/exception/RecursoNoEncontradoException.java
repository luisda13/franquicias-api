package com.franquicias.franquicias_api.domain.exception;

public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String recurso, String identificador) {
        super(recurso + " con identificador '" + identificador + "' no encontrado.");
    }
}
