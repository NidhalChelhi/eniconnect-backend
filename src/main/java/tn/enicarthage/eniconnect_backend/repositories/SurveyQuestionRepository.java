package tn.enicarthage.eniconnect_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.enicarthage.eniconnect_backend.entities.SurveyQuestion;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, UUID> {

    List<SurveyQuestion> findBySurveyIdOrderByDisplayOrderAsc(UUID surveyId);

    // findBySurveyId
    @Query("SELECT eq FROM SurveyQuestion eq WHERE eq.survey.id = :surveyId")
    List<SurveyQuestion> findBySurveyId(UUID surveyId);

    @Modifying
    @Query("DELETE FROM SurveyQuestion eq WHERE eq.survey.id = :surveyId AND eq.question.id IN :questionIds")
    void deleteBySurveyIdAndQuestionIdIn(UUID surveyId, List<UUID> questionIds);

    boolean existsBySurveyIdAndQuestionId(UUID surveyId, UUID questionId);

    @Query("SELECT eq FROM SurveyQuestion eq WHERE eq.survey.id = :surveyId AND eq.question.id = :questionId")
    Optional<SurveyQuestion> findBySurveyIdAndQuestionId(UUID surveyId, UUID questionId);

    @Query("SELECT COUNT(eq) FROM SurveyQuestion eq WHERE eq.survey.id = :surveyId")
    int countBySurveyId(UUID surveyId);
}
