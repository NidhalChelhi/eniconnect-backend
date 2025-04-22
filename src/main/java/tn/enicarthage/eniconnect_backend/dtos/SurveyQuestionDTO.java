package tn.enicarthage.eniconnect_backend.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record SurveyQuestionDTO(
        UUID id,
        QuestionDTO question,
        Integer displayOrder,
        LocalDateTime createdAt
) {
}
