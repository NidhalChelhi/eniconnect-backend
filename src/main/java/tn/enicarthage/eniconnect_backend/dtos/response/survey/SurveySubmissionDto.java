package tn.enicarthage.eniconnect_backend.dtos.response.survey;

import java.time.LocalDateTime;

public record SurveySubmissionDto(
        Long id,
        Long studentId,
        String openFeedback,
        LocalDateTime submittedAt,
        boolean isSubmitted
) {
}