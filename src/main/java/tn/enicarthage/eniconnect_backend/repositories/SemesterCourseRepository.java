package tn.enicarthage.eniconnect_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.enicarthage.eniconnect_backend.entities.Semester;
import tn.enicarthage.eniconnect_backend.entities.SemesterCourse;
import tn.enicarthage.eniconnect_backend.entities.Specialization;

import java.util.List;

public interface SemesterCourseRepository extends JpaRepository<SemesterCourse, Long> {
    List<SemesterCourse> findBySemesterAndSpecializationAndYearOfStudy(
            Semester semester, Specialization specialization, Integer yearOfStudy);


}