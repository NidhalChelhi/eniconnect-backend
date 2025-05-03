package tn.enicarthage.eniconnect_backend.dtos.request.survey;

import jakarta.validation.constraints.NotNull;

public record PublishSurveyDto(
        @NotNull Boolean isPublished
) {
}