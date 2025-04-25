package tn.enicarthage.eniconnect_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.enicarthage.eniconnect_backend.entities.Semester;
import tn.enicarthage.eniconnect_backend.entities.SemesterCourse;
import tn.enicarthage.eniconnect_backend.entities.Specialization;

import java.util.List;

public interface SemesterCourseRepository extends JpaRepository<SemesterCourse, Long> {
    List<SemesterCourse> findBySemesterAndSpecializationAndYearOfStudy(
            Semester semester, Specialization specialization, Integer yearOfStudy);

    @Query("SELECT sc FROM SemesterCourse sc " +
            "WHERE sc.semester.id = :semesterId " +
            "AND sc.specialization.id = :specializationId " +
            "AND sc.yearOfStudy = :yearOfStudy")
    List<SemesterCourse> findBySemesterAndSpecializationAndYear(
            @Param("semesterId") Long semesterId,
            @Param("specializationId") Long specializationId,
            @Param("yearOfStudy") Integer yearOfStudy);

}