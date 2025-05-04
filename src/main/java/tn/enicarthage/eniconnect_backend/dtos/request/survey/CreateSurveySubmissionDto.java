package tn.enicarthage.eniconnect_backend.dtos.request.survey;

import jakarta.validation.constraints.*;
import java.util.List;

public record CreateSurveySubmissionDto(
        @NotNull Long surveyId,
        @NotEmpty List<CourseResponseDto> courseResponses,
        String openFeedback
) {
    public record CourseResponseDto(
            @NotNull Long courseId,
            @Size(min = 8, max = 8, message = "Exactly 8 answers required")
            @NotNull List<@Min(1) @Max(4) Integer> answers
    ) {
    }
}