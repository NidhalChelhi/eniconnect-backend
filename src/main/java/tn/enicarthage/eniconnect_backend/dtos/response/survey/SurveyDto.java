package tn.enicarthage.eniconnect_backend.dtos.response.survey;

import tn.enicarthage.eniconnect_backend.enums.Speciality;

import java.time.LocalDateTime;
import java.util.Set;

public record SurveyDto(
        Long id,
        String title,
        Speciality speciality,
        int semester,
        int level,
        String schoolYear,
        boolean isPublished,
        LocalDateTime openDate,
        LocalDateTime closeDate,
        Set<Long> courseIds,
        boolean isActive
) {
}