package tn.enicarthage.eniconnect_backend.services;

import tn.enicarthage.eniconnect_backend.dtos.request.survey.CreateSurveyDto;

import java.util.List;

public interface SurveySeederService {
    void seedAllPossibleSurveys();

    List<CreateSurveyDto> generateAllPossibleSurveyTemplates();
}