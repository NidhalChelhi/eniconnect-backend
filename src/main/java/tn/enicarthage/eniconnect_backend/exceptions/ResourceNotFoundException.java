// File: src/main/java/tn/enicarthage/eniconnect_backend/exceptions/ResourceNotFoundException.java
package tn.enicarthage.eniconnect_backend.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}