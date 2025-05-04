package tn.enicarthage.eniconnect_backend.dtos.request.survey;

import java.time.LocalDateTime;

public record UpdateSurveyDatesDto(
        LocalDateTime newOpenDate,
        LocalDateTime newCloseDate
) {
    public UpdateSurveyDatesDto {
        // Validate only if both dates are provided
        if (newOpenDate != null && newCloseDate != null && newOpenDate.isAfter(newCloseDate)) {
            throw new IllegalArgumentException("Open date must be before close date");
        }
    }
}
