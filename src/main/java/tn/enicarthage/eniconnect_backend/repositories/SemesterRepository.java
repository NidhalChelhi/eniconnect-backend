package tn.enicarthage.eniconnect_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.enicarthage.eniconnect_backend.entities.AcademicYear;
import tn.enicarthage.eniconnect_backend.entities.Semester;

import java.util.List;

public interface SemesterRepository extends JpaRepository<Semester, Long> {
    List<Semester> findByAcademicYear(AcademicYear academicYear);
    Semester findByAcademicYearAndNumber(AcademicYear academicYear, Integer number);
}