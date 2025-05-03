package tn.enicarthage.eniconnect_backend.dtos.response.question;

import java.time.LocalDateTime;

public record QuestionTemplateResponseDto(
        Long id,
        String text,
        int minValue,
        int maxValue,
        int orderIndex,
        LocalDateTime createdAt
) {
}