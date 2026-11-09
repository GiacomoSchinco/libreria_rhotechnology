package com.rhotechnologylibri.libreria_rhotechnology.exception;

/**
 * Risorsa non trovata (HTTP 404).
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resource, Integer id) {
        super(resource + " con id " + id + " non esiste");
    }
}
