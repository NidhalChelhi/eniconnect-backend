package tn.enicarthage.eniconnect_backend.dtos.request.student;

import jakarta.validation.constraints.*;
import tn.enicarthage.eniconnect_backend.enums.Speciality;

public record CreateStudentDto(
        @NotBlank String matricule,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @Email @NotBlank String email,
        @NotBlank Speciality speciality,
        @NotBlank @Min(1) @Max(3) int currentLevel,
        @NotBlank @Pattern(regexp = "[A-D]", message = "Groupe must be A, B, C, or D")
        String groupe,
        @NotBlank @Pattern(regexp = "\\d{4}/\\d{4}", message = "Invalid school year format")
        String entrySchoolYear,
        String gender
) {
}