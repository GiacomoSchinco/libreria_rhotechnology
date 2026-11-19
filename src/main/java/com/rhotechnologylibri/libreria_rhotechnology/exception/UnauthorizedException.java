package com.rhotechnologylibri.libreria_rhotechnology.exception;

/**
 * Autenticazione assente o credenziali non valide (HTTP 401).
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
