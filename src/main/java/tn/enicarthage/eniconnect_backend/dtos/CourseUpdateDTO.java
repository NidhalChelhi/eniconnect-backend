package tn.enicarthage.eniconnect_backend.dtos;

import jakarta.validation.constraints.NotNull;
import tn.enicarthage.eniconnect_backend.enums.Filiere;
import tn.enicarthage.eniconnect_backend.enums.Semester;

public record CourseUpdateDTO(
        @NotNull(message = "Course code is required")
        String courseCode,

        @NotNull(message = "Course name is required")
        String courseName,

        @NotNull(message = "Filiere is required")
        Filiere filiere,

        @NotNull(message = "Semester is required")
        Semester semester) {
}
