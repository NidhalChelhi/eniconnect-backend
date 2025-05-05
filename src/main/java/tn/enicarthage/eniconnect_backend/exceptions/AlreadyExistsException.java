package tn.enicarthage.eniconnect_backend.exceptions;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s already exists with %s: %s", resourceName, fieldName, fieldValue));
    }
}