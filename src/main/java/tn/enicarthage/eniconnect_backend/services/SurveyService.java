package tn.enicarthage.eniconnect_backend.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tn.enicarthage.eniconnect_backend.dtos.request.survey.CreateSurveyDto;
import tn.enicarthage.eniconnect_backend.dtos.request.survey.CreateSurveySubmissionDto;
import tn.enicarthage.eniconnect_backend.dtos.request.survey.SurveyFilterParams;
import tn.enicarthage.eniconnect_backend.dtos.request.survey.UpdateSurveyDatesDto;
import tn.enicarthage.eniconnect_backend.dtos.response.student.StudentDto;
import tn.enicarthage.eniconnect_backend.dtos.response.survey.SurveyDto;
import tn.enicarthage.eniconnect_backend.dtos.response.survey.SurveyStatsDto;
import tn.enicarthage.eniconnect_backend.dtos.response.survey.SurveySubmissionDetailsDto;

import java.util.List;

@Service
public interface SurveyService {

    SurveyDto updateSurveyDates(Long surveyId, UpdateSurveyDatesDto updateDatesDto);

    SurveyDto getSurveyById(Long id);

    List<SurveyDto> getAllSurveys();

    Page<SurveyDto> getAllSurveys(Pageable pageable);

    SurveyDto createSurvey(CreateSurveyDto CreateSurveyDto);

    void deleteSurvey(Long id);

    SurveyDto publishSurvey(Long id);

    SurveyDto unpublishSurvey(Long id);

    SurveySubmissionDetailsDto submitSurveyResponse(Long surveyId, CreateSurveySubmissionDto createSurveySubmissionDto, Long studentId);

    SurveySubmissionDetailsDto getStudentResponseForSurvey(Long surveyId, Long studentId);

    List<SurveyDto> getActiveSurveysForStudent(Long studentId);

    Long getEligibleStudentsCount(Long surveyId);

    Page<SurveyDto> getAllSurveys(SurveyFilterParams filterParams, Pageable pageable);

    SurveyStatsDto getSurveyStats(Long surveyId);

    Page<StudentDto> getEligibleStudents(Long surveyId, Pageable pageable);

    Page<SurveySubmissionDetailsDto> getSurveySubmissions(Long surveyId, Pageable pageable);
}
