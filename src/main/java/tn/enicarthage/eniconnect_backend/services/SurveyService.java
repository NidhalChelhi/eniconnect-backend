package tn.enicarthage.eniconnect_backend.services;


import tn.enicarthage.eniconnect_backend.dtos.base.CourseDTO;
import tn.enicarthage.eniconnect_backend.dtos.request_response.CreateSurveyRequestDTO;
import tn.enicarthage.eniconnect_backend.dtos.request_response.SurveyStatsDTO;
import tn.enicarthage.eniconnect_backend.dtos.survey.SurveyDTO;
import tn.enicarthage.eniconnect_backend.dtos.survey.SurveyQuestionDTO;
import tn.enicarthage.eniconnect_backend.dtos.survey.SurveyWithQuestionsDTO;

import java.util.List;

public interface SurveyService {
    SurveyDTO createSurvey(CreateSurveyRequestDTO request);

    SurveyDTO getSurveyById(Long id);

    List<SurveyDTO> getActiveSurveys();

    List<SurveyDTO> getSurveysBySpecializationAndYear(String specializationCode, int yearOfStudy);

    SurveyStatsDTO getSurveyStats(Long surveyId);

    void activateSurvey(Long surveyId);

    void closeSurvey(Long surveyId);

    SurveyWithQuestionsDTO getSurveyWithQuestions(Long id);

    List<SurveyQuestionDTO> getSurveyTemplateQuestions(Long templateId);

    List<SurveyDTO> getSurveysForStudent(Long studentId);

    List<CourseDTO> getCoursesForSurvey(Long surveyId);
}
