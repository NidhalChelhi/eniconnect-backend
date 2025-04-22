package tn.enicarthage.eniconnect_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.enicarthage.eniconnect_backend.entities.CourseResponse;
import tn.enicarthage.eniconnect_backend.entities.QuestionResponse;

import java.util.List;

public interface QuestionResponseRepository extends JpaRepository<QuestionResponse, Long> {
    List<QuestionResponse> findByCourseResponse(CourseResponse courseResponse);
}