package tn.enicarthage.eniconnect_backend.mappers;

import org.springframework.stereotype.Component;
import tn.enicarthage.eniconnect_backend.dtos.request.survey.CreateSurveyDto;
import tn.enicarthage.eniconnect_backend.dtos.response.survey.SurveyDto;
import tn.enicarthage.eniconnect_backend.entities.Course;
import tn.enicarthage.eniconnect_backend.entities.Survey;

import java.util.List;
import java.util.Set;

@Component
public class SurveyMapper {
    public SurveyDto toDto(Survey survey) {
        return new SurveyDto(
                survey.getId(),
                survey.getTitle(),
                survey.getSpeciality(),
                survey.getSemester(),
                survey.getLevel(),
                survey.getSchoolYear(),
                survey.isPublished(),
                survey.getOpenDate(),
                survey.getCloseDate(),
                survey.getTargetCourses(),
                survey.isActive(),
                survey.getResponses(),
                survey.getCreatedAt()
        );
    }

    public Survey toEntity(CreateSurveyDto dto, Set<Course> courses) {
        return Survey.builder()
                .title(dto.title())
                .speciality(dto.speciality())
                .semester(dto.semester())
                .level(dto.level())
                .schoolYear(dto.schoolYear())
                .isPublished(false)
                .openDate(dto.openDate())
                .closeDate(dto.closeDate())
                .targetCourses(courses)
                .responses(List.of())
                .build();
    }
}