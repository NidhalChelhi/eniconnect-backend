package tn.enicarthage.eniconnect_backend.dtos;

import tn.enicarthage.eniconnect_backend.enums.QuestionType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record QuestionDTO(
        UUID id,
        String text,
        QuestionType type,
        List<String> options,
        LocalDateTime createdAt
) {
}
