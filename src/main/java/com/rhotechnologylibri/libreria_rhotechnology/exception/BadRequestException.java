package com.rhotechnologylibri.libreria_rhotechnology.exception;

/**
 * Richiesta non valida (HTTP 400), ad esempio un riferimento inesistente.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
