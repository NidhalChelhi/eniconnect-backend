package tn.enicarthage.eniconnect_backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.enicarthage.eniconnect_backend.entities.Student;
import tn.enicarthage.eniconnect_backend.enums.Speciality;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByMatricule(String matricule);

    Optional<Student> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByMatricule(String matricule);

    List<Student> findAll();

    Page<Student> findAll(Pageable pageable);

    long countBySpecialityAndCurrentLevelAndEntrySchoolYearIn(
            Speciality speciality,
            int currentLevel,
            List<String> entrySchoolYears
    );

    Page<Student> findBySpecialityAndCurrentLevelAndEntrySchoolYearIn(
            Speciality speciality,
            int currentLevel,
            List<String> entrySchoolYears,
            Pageable pageable
    );
}