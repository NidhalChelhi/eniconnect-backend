package tn.enicarthage.eniconnect_backend.dtos.request.survey;

import jakarta.validation.constraints.*;
import tn.enicarthage.eniconnect_backend.enums.Speciality;
import java.time.LocalDateTime;

public record CreateSurveyDto(
        @NotBlank String title,
        @NotNull Speciality speciality,
        @Min(1) @Max(2) int semester,
        @Min(1) @Max(3) int level,
        @Pattern(regexp = "\\d{4}/\\d{4}") String schoolYear,
        LocalDateTime openDate,
        LocalDateTime closeDate
) {
        public CreateSurveyDto {
                if (openDate != null && closeDate != null && openDate.isAfter(closeDate)) {
                        throw new IllegalArgumentException("Open date must be before close date");
                }
        }
}
