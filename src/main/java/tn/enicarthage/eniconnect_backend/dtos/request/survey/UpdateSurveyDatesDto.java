package tn.enicarthage.eniconnect_backend.dtos.request.survey;

import java.time.LocalDateTime;

public record UpdateSurveyDatesDto(
        LocalDateTime newOpenDate,  // can be null
        LocalDateTime newCloseDate  // can be null
) {
  /*  public void validateForPublishedSurvey() {
        if (newOpenDate != null ) {
            throw new IllegalArgumentException("Cannot change open date for published survey");
        }
    }
*/
    public boolean isValid() {
        LocalDateTime now = LocalDateTime.now();

        // Both null is valid
        if (newOpenDate == null && newCloseDate == null) return true;

        // Validate newOpenDate if provided
        if (newOpenDate != null) {
            if (newOpenDate.isBefore(now)) {
                throw new IllegalArgumentException("Open date must be in the future");
            }
        }

        // Validate newCloseDate if provided
        if (newCloseDate != null) {
            if (newCloseDate.isBefore(now)) {
                throw new IllegalArgumentException("Close date must be in the future");
            }
        }

        // If both dates are provided
        if (newOpenDate != null && newCloseDate != null) {
            if (!newOpenDate.isBefore(newCloseDate)) {
                throw new IllegalArgumentException("Open date must be before close date");
            }
        }

        return true;
    }
}