package tn.enicarthage.eniconnect_backend.dtos.request.course;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import tn.enicarthage.eniconnect_backend.enums.Speciality;

public record CreateCourseDto(
        @NotBlank String code,
        @NotBlank String name,
        @NotBlank Speciality speciality,
        @NotBlank @Min(1) @Max(2) int semester,
        @NotBlank @Min(1) @Max(3) int level
) {
}

