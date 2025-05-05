package tn.enicarthage.eniconnect_backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.enicarthage.eniconnect_backend.entities.Answer;
import tn.enicarthage.eniconnect_backend.entities.Student;
import tn.enicarthage.eniconnect_backend.entities.Survey;
import tn.enicarthage.eniconnect_backend.entities.SurveySubmission;

import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyResponseRepository extends JpaRepository<SurveySubmission, Long> {

    boolean existsByStudentAndSurvey(Student student, Survey survey);

    @Query("SELECT sr FROM SurveySubmission sr LEFT JOIN FETCH sr.answers WHERE sr.student.id = :studentId AND sr.survey.id = :surveyId")
    Optional<SurveySubmission> findByStudentIdAndSurveyId(Long studentId, Long surveyId);

    long countBySurveyId(Long surveyId);

    @Query("SELECT a FROM Answer a WHERE a.submission.survey.id = :surveyId AND a.course.id = :courseId")
    List<Answer> findAnswersBySurveyAndCourse(@Param("surveyId") Long surveyId, @Param("courseId") Long courseId);

    @Query("SELECT a FROM Answer a WHERE a.submission.survey.id = :surveyId AND a.course.id = :courseId AND a.question.id = :questionId")
    List<Answer> findAnswersBySurveyAndCourseAndQuestion(
            @Param("surveyId") Long surveyId,
            @Param("courseId") Long courseId,
            @Param("questionId") Long questionId);

    @Query("SELECT a FROM Answer a WHERE a.submission.survey.id = :surveyId")
    List<Answer> findAllBySurveyId(@Param("surveyId") Long surveyId);

    Page<SurveySubmission> findBySurveyId(Long surveyId, Pageable pageable);
}