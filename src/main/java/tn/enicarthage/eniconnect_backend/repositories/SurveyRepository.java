package tn.enicarthage.eniconnect_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.enicarthage.eniconnect_backend.entities.Specialization;
import tn.enicarthage.eniconnect_backend.entities.Survey;

import java.util.List;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
    List<Survey> findByIsActiveTrue();

    List<Survey> findBySpecializationAndYearOfStudy(Specialization specialization, Integer yearOfStudy);
}