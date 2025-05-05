package tn.enicarthage.eniconnect_backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.enicarthage.eniconnect_backend.entities.Course;
import tn.enicarthage.eniconnect_backend.entities.Survey;
import tn.enicarthage.eniconnect_backend.enums.Speciality;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {

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


    boolean existsBySpecialityAndLevelAndSemesterAndSchoolYear(
            Speciality speciality,
            int level,
            int semester,
            String schoolYear
    );

    @Query("SELECT s FROM Survey s LEFT JOIN FETCH s.targetCourses WHERE s.id = :id")
    Optional<Survey> findByIdWithCourses(Long id);


    List<Survey> findByIsPublished(boolean isPublished);

    @Query("SELECT s FROM Survey s LEFT JOIN FETCH s.targetCourses WHERE s.isPublished = true")
    List<Survey> findAllPublishedWithCourses();

    Page<Survey> findAll(Pageable pageable);

    List<Survey> findAllByTargetCoursesContaining(Course course);

    @Query("SELECT s FROM Survey s WHERE " +
            "(:speciality IS NULL OR s.speciality = :speciality) AND " +
            "(:schoolYear IS NULL OR s.schoolYear = :schoolYear) AND " +
            "(:level IS NULL OR s.level = :level) AND " +
            "(:semester IS NULL OR s.semester = :semester) AND " +
            "(:isPublished IS NULL OR s.isPublished = :isPublished) AND " +
            "(:isActive IS NULL OR " +
            "  (:isActive = true AND s.openDate IS NOT NULL AND s.openDate <= CURRENT_TIMESTAMP AND (s.closeDate IS NULL OR s.closeDate >= CURRENT_TIMESTAMP)) OR " +
            "  (:isActive = false AND (s.openDate IS NULL OR s.openDate > CURRENT_TIMESTAMP OR (s.closeDate IS NOT NULL AND s.closeDate < CURRENT_TIMESTAMP)))" +
            ")")
    Page<Survey> findAllWithFilters(
            @Param("speciality") Speciality speciality,
            @Param("schoolYear") String schoolYear,
            @Param("level") Integer level,
            @Param("semester") Integer semester,
            @Param("isPublished") Boolean isPublished,
            @Param("isActive") Boolean isActive,
            Pageable pageable
    );
}
