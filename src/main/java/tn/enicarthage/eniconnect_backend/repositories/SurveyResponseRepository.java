package tn.enicarthage.eniconnect_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.enicarthage.eniconnect_backend.entities.SurveyResponse;

import java.util.List;
import java.util.Optional;

public interface SurveyResponseRepository extends JpaRepository<SurveyResponse, Long> {
    Optional<SurveyResponse> findBySurveyIdAndStudentId(Long surveyId, Long studentId);

    boolean existsBySurveyIdAndStudentId(Long surveyId, Long studentId);

    List<SurveyResponse> findBySurveyId(Long surveyId);
}