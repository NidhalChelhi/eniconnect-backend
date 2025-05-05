package tn.enicarthage.eniconnect_backend.dtos.request.survey;

import java.time.LocalDateTime;

public record UpdateSurveyDatesDto(
        LocalDateTime newOpenDate,
        LocalDateTime newCloseDate
) {
}