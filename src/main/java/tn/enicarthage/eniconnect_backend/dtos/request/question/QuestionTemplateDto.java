package tn.enicarthage.eniconnect_backend.dtos.request.question;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record QuestionTemplateDto(
        @NotBlank @Size(max = 500) String text,
        @Min(1) @Max(1) int minValue,
        @Min(4) @Max(4) int maxValue,
        @Min(1) @Max(8) int orderIndex
) {
}