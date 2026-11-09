package com.rhotechnologylibri.libreria_rhotechnology.exception;

/**
 * Risorsa ancora referenziata da altre entita (HTTP 409): la cancellazione non e' consentita.
 */
public class ResourceInUseException extends RuntimeException {

    public ResourceInUseException(String message) {
        super(message);
    }

    public ResourceInUseException(String resource, Integer id, String usedBy) {
        super(resource + " con id " + id + " e' usata da " + usedBy + ": cancellazione non consentita");
    }
}
