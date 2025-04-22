package tn.enicarthage.eniconnect_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.enicarthage.eniconnect_backend.entities.CourseResponse;
import tn.enicarthage.eniconnect_backend.entities.SurveyResponse;

import java.util.List;

public interface CourseResponseRepository extends JpaRepository<CourseResponse, Long> {
    List<CourseResponse> findBySurveyResponse(SurveyResponse surveyResponse);
}