// File: src/main/java/tn/enicarthage/eniconnect_backend/exceptions/ValidationException.java
package tn.enicarthage.eniconnect_backend.exceptions;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}