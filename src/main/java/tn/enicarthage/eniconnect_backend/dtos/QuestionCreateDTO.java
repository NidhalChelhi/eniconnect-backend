package tn.enicarthage.eniconnect_backend.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tn.enicarthage.eniconnect_backend.enums.QuestionType;

import java.util.List;

public record QuestionCreateDTO(
        @NotEmpty(message = "Question text is required")
        String text,

        @NotNull(message = "Question type is required")
        QuestionType type,

        List<@NotEmpty String> options
) {
    public boolean isValid() {
        return type != QuestionType.LIKERT_4 || (options != null && !options.isEmpty());
    }
}

