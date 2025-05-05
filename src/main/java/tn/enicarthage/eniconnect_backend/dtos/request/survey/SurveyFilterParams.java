package tn.enicarthage.eniconnect_backend.dtos.request.survey;

import tn.enicarthage.eniconnect_backend.enums.Speciality;

public record SurveyFilterParams(
        Speciality speciality,
        String schoolYear,
        Integer level,
        Integer semester,
        Boolean isPublished,
        Boolean isActive
) {
}