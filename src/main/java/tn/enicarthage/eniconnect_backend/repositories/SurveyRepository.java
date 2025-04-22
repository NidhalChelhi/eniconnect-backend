package tn.enicarthage.eniconnect_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.enicarthage.eniconnect_backend.entities.Course;
import tn.enicarthage.eniconnect_backend.entities.Survey;

import java.util.List;
import java.util.UUID;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, UUID> {

    List<Survey> findByCoursesContaining(Course course);
}
