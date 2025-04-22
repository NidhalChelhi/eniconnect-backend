package tn.enicarthage.eniconnect_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.enicarthage.eniconnect_backend.entities.SurveyTemplate;

import java.util.List;

public interface SurveyTemplateRepository extends JpaRepository<SurveyTemplate, Long> {
    List<SurveyTemplate> findByNameContaining(String name);
}