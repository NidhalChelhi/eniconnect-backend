package tn.enicarthage.eniconnect_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.enicarthage.eniconnect_backend.entities.Survey;
import tn.enicarthage.eniconnect_backend.enums.Speciality;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {

    // Find surveys for student dashboard
    @Query("SELECT s FROM Survey s WHERE " +
            "s.speciality = :speciality AND " +
            "s.level <= :currentLevel AND " +
            "s.schoolYear IN :schoolYears AND " +
            "(s.isPublished = true) AND " +
            "(s.openDate IS NULL OR s.openDate <= :now) AND " +
            "(s.closeDate IS NULL OR s.closeDate >= :now)")
    List<Survey> findVisibleSurveys(
            Speciality speciality,
            int currentLevel,
            List<String> schoolYears,
            LocalDateTime now
    );

    // Check if survey exists for same criteria (avoid duplicates)
    boolean existsBySpecialityAndLevelAndSemesterAndSchoolYear(
            Speciality speciality,
            int level,
            int semester,
            String schoolYear
    );

    // Find with courses eager-loaded
    @Query("SELECT s FROM Survey s LEFT JOIN FETCH s.targetCourses WHERE s.id = :id")
    Optional<Survey> findByIdWithCourses(Long id);

    // find by isPublished

    List<Survey> findByIsPublished(boolean isPublished);

    @Query("SELECT s FROM Survey s LEFT JOIN FETCH s.targetCourses WHERE s.isPublished = true")
    List<Survey> findAllPublishedWithCourses();
}