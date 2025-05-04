package tn.enicarthage.eniconnect_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.enicarthage.eniconnect_backend.entities.Student;
import tn.enicarthage.eniconnect_backend.entities.Survey;
import tn.enicarthage.eniconnect_backend.entities.SurveySubmission;

import java.util.Optional;

@Repository
public interface SurveyResponseRepository extends JpaRepository<SurveySubmission, Long> {

    // Check if student already submitted a survey
    boolean existsByStudentAndSurvey(Student student, Survey survey);

    // Find response with answers eager-loaded
    @Query("SELECT sr FROM SurveySubmission sr LEFT JOIN FETCH sr.answers WHERE sr.id = :id")
    Optional<SurveySubmission> findByIdWithAnswers(Long id);

    // Count completed surveys per student
    @Query("SELECT COUNT(sr) FROM SurveySubmission sr WHERE sr.student = :student AND sr.isSubmitted = true")
    long countCompletedSurveysByStudent(Student student);


    @Query("SELECT sr FROM SurveySubmission sr LEFT JOIN FETCH sr.answers WHERE sr.student.id = :studentId AND sr.survey.id = :surveyId")
    Optional<SurveySubmission> findByStudentIdAndSurveyId(Long studentId, Long surveyId);
}