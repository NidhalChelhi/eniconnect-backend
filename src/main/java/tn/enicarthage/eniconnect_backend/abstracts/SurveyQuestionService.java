package tn.enicarthage.eniconnect_backend.abstracts;

import tn.enicarthage.eniconnect_backend.dtos.SurveyQuestionDTO;
import tn.enicarthage.eniconnect_backend.dtos.QuestionInSurveyDTO;

import java.util.List;
import java.util.UUID;

public interface SurveyQuestionService {
    List<SurveyQuestionDTO> findBySurveyId(UUID surveyId);

    List<SurveyQuestionDTO> addQuestionsToSurvey(UUID surveyId, List<QuestionInSurveyDTO> questions);

    void removeQuestionsFromSurvey(UUID surveyId, List<UUID> questionIds);

}
