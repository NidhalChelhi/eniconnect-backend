package tn.enicarthage.eniconnect_backend.dtos.request.survey;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateSurveySubmissionDto(
        @NotNull Long surveyId,
        @NotEmpty List<CourseResponseDto> courseResponses,
        String openFeedback
) {
    public record CourseResponseDto(
            @NotNull Long courseId,
            @NotNull List<Integer> answers
    ) {
    }
}