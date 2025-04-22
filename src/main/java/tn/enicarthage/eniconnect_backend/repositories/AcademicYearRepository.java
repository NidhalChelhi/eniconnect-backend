package tn.enicarthage.eniconnect_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.enicarthage.eniconnect_backend.entities.AcademicYear;
public interface AcademicYearRepository extends JpaRepository<AcademicYear, Long> {
    AcademicYear findByName(String name);
    AcademicYear findByStartYearAndEndYear(Integer startYear, Integer endYear);
}