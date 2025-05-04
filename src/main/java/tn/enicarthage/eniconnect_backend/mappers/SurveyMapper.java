package tn.enicarthage.eniconnect_backend.mappers;

import org.springframework.stereotype.Component;
import tn.enicarthage.eniconnect_backend.dtos.request.survey.CreateSurveyDto;
import tn.enicarthage.eniconnect_backend.dtos.response.survey.SurveyDto;
import tn.enicarthage.eniconnect_backend.entities.Course;
import tn.enicarthage.eniconnect_backend.entities.Survey;
import tn.enicarthage.eniconnect_backend.entities.SurveyResponse;

import java.time.LocalDateTime;
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
                survey.isActive()) ;
    }
    public Survey toEntity(SurveyDto surveyDto) {
        return new Survey(
                surveyDto.id() ,
                surveyDto.title() ,
                surveyDto.speciality(),
                surveyDto.semester() ,
                surveyDto.level(),
                surveyDto.schoolYear(),
                surveyDto.isPublished(),
                surveyDto.openDate(),
                surveyDto.closeDate(),
                LocalDateTime.now(),
                surveyDto.courses(),
                List.of());
    }
    public Survey toEntity(CreateSurveyDto createSurveyDto, Set<Course> courses) {
        Survey survey = new Survey();
        survey.setTitle(createSurveyDto.title());
        survey.setSpeciality(createSurveyDto.speciality());
        survey.setSemester(createSurveyDto.semester());
        survey.setLevel(createSurveyDto.level());
        survey.setSchoolYear(createSurveyDto.schoolYear());
        survey.setPublished(false);
        survey.setOpenDate(createSurveyDto.openDate());
        survey.setCloseDate(createSurveyDto.closeDate());
        survey.setCreatedAt(LocalDateTime.now());
        survey.setTargetCourses(courses);
        survey.setResponses(List.of());
        return survey;
    }
}
