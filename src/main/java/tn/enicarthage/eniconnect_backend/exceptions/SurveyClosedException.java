// File: src/main/java/tn/enicarthage/eniconnect_backend/exceptions/SurveyClosedException.java
package tn.enicarthage.eniconnect_backend.exceptions;

public class SurveyClosedException extends RuntimeException {
    public SurveyClosedException(String message) {
        super(message);
    }
}