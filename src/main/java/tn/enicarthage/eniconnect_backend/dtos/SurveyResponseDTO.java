package tn.enicarthage.eniconnect_backend.dtos;

import tn.enicarthage.eniconnect_backend.entities.Course;
import tn.enicarthage.eniconnect_backend.enums.Filiere;
import tn.enicarthage.eniconnect_backend.enums.Semester;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record SurveyResponseDTO(
        UUID id,
        String title,
        Filiere filiere,
        Semester semester,
        LocalDateTime openDate,
        LocalDateTime closeDate,
        List<Course> courses,
        List<SurveyQuestionDTO> questions,
        LocalDateTime createdAt
) {
}