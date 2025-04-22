package tn.enicarthage.eniconnect_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.enicarthage.eniconnect_backend.entities.SurveyQuestion;
import tn.enicarthage.eniconnect_backend.entities.SurveyTemplate;

import java.util.List;

public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, Long> {
    List<SurveyQuestion> findBySurveyTemplateOrderByQuestionOrder(SurveyTemplate template);
}