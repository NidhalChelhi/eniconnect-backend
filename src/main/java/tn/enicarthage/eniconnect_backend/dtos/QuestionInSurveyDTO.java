package tn.enicarthage.eniconnect_backend.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;


public record QuestionInSurveyDTO(
        @NotNull(message = "Question ID is required")
        UUID questionId,

        @NotNull(message = "Display order is required")
        Integer displayOrder
) {
}

