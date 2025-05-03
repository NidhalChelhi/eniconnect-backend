package tn.enicarthage.eniconnect_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.enicarthage.eniconnect_backend.entities.QuestionTemplate;

import java.util.List;

@Repository
public interface QuestionTemplateRepository extends JpaRepository<QuestionTemplate, Long> {

    // Get all questions ordered by display order
    @Query("SELECT q FROM QuestionTemplate q ORDER BY q.orderIndex ASC")
    List<QuestionTemplate> findAllOrdered();

    // Check if order index is already used
    boolean existsByOrderIndex(int orderIndex);
}