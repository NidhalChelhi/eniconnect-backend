package tn.enicarthage.eniconnect_backend.abstracts;

import tn.enicarthage.eniconnect_backend.dtos.SurveyCreateDTO;
import tn.enicarthage.eniconnect_backend.dtos.SurveyResponseDTO;

import java.util.List;
import java.util.UUID;

public interface SurveyService {
    List<SurveyResponseDTO> findAll();

    SurveyResponseDTO findOne(UUID courseId);

    SurveyResponseDTO createOne(SurveyCreateDTO surveyCreateDTO);

    void deleteOne(UUID surveyId);

    boolean isSurveyOpen(UUID surveyId);
}
