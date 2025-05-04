package tn.enicarthage.eniconnect_backend.services;

import tn.enicarthage.eniconnect_backend.dtos.request.question.QuestionTemplateDto;
import tn.enicarthage.eniconnect_backend.entities.QuestionTemplate;

import java.util.List;

public interface QuestionService {
        List<QuestionTemplate> getAllQuestions();
        QuestionTemplate createQuestion(QuestionTemplateDto question);
        QuestionTemplate updateeQuestionById(long id , QuestionTemplateDto question);
        void DeleteQuestion(Long id);

}
