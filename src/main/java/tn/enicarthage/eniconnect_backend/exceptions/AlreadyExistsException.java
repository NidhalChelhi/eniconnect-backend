// File: src/main/java/tn/enicarthage/eniconnect_backend/exceptions/AlreadyExistsException.java
package tn.enicarthage.eniconnect_backend.exceptions;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}