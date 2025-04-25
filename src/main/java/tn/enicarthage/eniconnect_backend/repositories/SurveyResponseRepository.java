package tn.enicarthage.eniconnect_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.enicarthage.eniconnect_backend.entities.SurveyResponse;

import java.util.List;
import java.util.Optional;

public interface SurveyResponseRepository extends JpaRepository<SurveyResponse, Long> {
    Optional<SurveyResponse> findBySurveyIdAndStudentId(Long surveyId, Long studentId);

    boolean existsBySurveyIdAndStudentId(Long surveyId, Long studentId);

    List<SurveyResponse> findBySurveyId(Long surveyId);


    @Query("SELECT COUNT(r) FROM SurveyResponse r WHERE r.survey.id = :surveyId")
    int countBySurveyId(@Param("surveyId") Long surveyId);

    @Query("SELECT r FROM SurveyResponse r JOIN FETCH r.student s JOIN FETCH s.user WHERE r.survey.id = :surveyId")
    List<SurveyResponse> findWithStudentDetailsBySurveyId(@Param("surveyId") Long surveyId);
}