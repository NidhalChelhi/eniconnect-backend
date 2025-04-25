package tn.enicarthage.eniconnect_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.enicarthage.eniconnect_backend.entities.Specialization;
import tn.enicarthage.eniconnect_backend.entities.Survey;

import java.util.List;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
    List<Survey> findByIsActiveTrue();

    List<Survey> findBySpecializationAndYearOfStudy(Specialization specialization, Integer yearOfStudy);

    @Query("SELECT s FROM Survey s " +
            "JOIN s.semester sem " +
            "JOIN sem.academicYear ay " +
            "WHERE s.specialization.id = :specializationId " +
            "AND ay.startYear >= :startYear " +
            "AND (ay.endYear <= :endYear OR :endYear IS NULL) " +
            "AND s.yearOfStudy = :yearOfStudy")
    List<Survey> findBySpecializationAndAcademicYears(
            @Param("specializationId") Long specializationId,
            @Param("startYear") Integer startYear,
            @Param("endYear") Integer endYear,
            @Param("yearOfStudy") Integer yearOfStudy);

    // find by specialization
    List<Survey> findBySpecialization(Specialization specialization);
}
