package com.franquicias.franquicias_api.domain.exception;

// Hereda de RuntimeException, usada para mapear a 409 Conflict
public class RecursoDuplicadoException extends RuntimeException {

    // Este constructor recibe el mensaje final EXACTO que el usuario verá.
    public RecursoDuplicadoException(String message) {
        super(message);
    }
}
