package tn.enicarthage.eniconnect_backend.services;

import tn.enicarthage.eniconnect_backend.dtos.request_response.SurveyStatsDTO;
import tn.enicarthage.eniconnect_backend.dtos.survey.SurveyResponseDTO;
import tn.enicarthage.eniconnect_backend.dtos.survey.SurveyResponseSummaryDTO;
import tn.enicarthage.eniconnect_backend.dtos.survey.SurveySubmissionDTO;

import java.util.List;

public interface SurveyResponseService {
    void submitSurveyResponse(SurveySubmissionDTO submission, Long studentId);

    boolean hasStudentCompletedSurvey(Long surveyId, Long studentId);

    SurveyResponseDTO getStudentResponse(Long surveyId, Long studentId);

    List<SurveyResponseSummaryDTO> getSurveyResponses(Long surveyId);

    SurveyStatsDTO getSurveyStats(Long surveyId);
}