package com.rhotechnologylibri.libreria_rhotechnology.exception;

/**
 * Risorsa duplicata (HTTP 409), tipicamente per un vincolo UNIQUE.
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String resource, String field, Object value) {
        super(resource + " con " + field + " '" + value + "' esiste gia");
    }
}
