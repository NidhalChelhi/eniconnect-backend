package tn.enicarthage.eniconnect_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.enicarthage.eniconnect_backend.entities.Course;
import tn.enicarthage.eniconnect_backend.enums.Speciality;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // Find courses by speciality, level, and semester
    List<Course> findBySpecialityAndLevelAndSemester(
            Speciality speciality,
            int level,
            int semester
    );

    // Optimized query for survey course assignment
    @Query("SELECT c FROM Course c WHERE c.speciality = :speciality " +
            "AND c.level = :level AND c.semester = :semester " +
            "ORDER BY c.code")
    List<Course> findCoursesForSurvey(
            Speciality speciality,
            int level,
            int semester
    );

    // Check if course code exists (for validation)
    boolean existsByCode(String code);

    Optional<Course> findByCode(String code);
}