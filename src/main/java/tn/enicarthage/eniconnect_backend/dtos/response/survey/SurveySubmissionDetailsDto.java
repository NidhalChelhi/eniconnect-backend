package tn.enicarthage.eniconnect_backend.dtos.response.survey;

import java.time.LocalDateTime;
import java.util.List;

public record SurveySubmissionDetailsDto(
        Long id,
        Long studentId,
        Long surveyId,
        List<AnswerDto> answers,
        String openFeedback,
        LocalDateTime submittedAt,
        boolean isSubmitted
) {
    public record AnswerDto(
            Long questionId,
            Long courseId,
            int rating
    ) {
    }
}