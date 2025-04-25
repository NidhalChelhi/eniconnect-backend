// File: src/main/java/tn/enicarthage/eniconnect_backend/exceptions/AccessDeniedException.java
package tn.enicarthage.eniconnect_backend.exceptions;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }
}