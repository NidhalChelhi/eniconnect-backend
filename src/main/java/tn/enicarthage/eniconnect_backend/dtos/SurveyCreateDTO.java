package tn.enicarthage.eniconnect_backend.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import tn.enicarthage.eniconnect_backend.enums.Filiere;
import tn.enicarthage.eniconnect_backend.enums.Semester;

import java.time.LocalDateTime;
import java.util.List;

public record SurveyCreateDTO(
        @NotNull(message = "Title is required")
        String title,

        @NotNull(message = "Filiere is required")
        Filiere filiere,

        @NotNull(message = "Semester is required")
        Semester semester,


        @NotNull(message = "Open date is required")
        @FutureOrPresent(message = "Open date must be in present or future")
        LocalDateTime openDate,

        @NotNull(message = "Close date is required")
        @Future(message = "Close date must be in future")
        LocalDateTime closeDate,

        @NotNull(message = "Questions are required")
        @Size(min = 1, message = "At least one question must be added")
        List<QuestionInSurveyDTO> questions
) {
}
