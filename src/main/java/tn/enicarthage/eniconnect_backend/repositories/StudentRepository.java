package tn.enicarthage.eniconnect_backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.enicarthage.eniconnect_backend.entities.Student;
import tn.enicarthage.eniconnect_backend.enums.Speciality;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Find by matricule (unique)
    Optional<Student> findByMatricule(String matricule);

    // Find by email (unique)
    Optional<Student> findByEmail(String email);

    // Find students by speciality and level (for bulk operations)
    List<Student> findBySpecialityAndCurrentLevel(
            Speciality speciality,
            int currentLevel
    );

    // Check if email or matricule exists (for validation)
    boolean existsByEmail(String email);

    boolean existsByMatricule(String matricule);


    // find all
    List<Student> findAll();

    Page<Student> findAll(Pageable pageable);

    // find by speciality
    List<Student> findBySpeciality(Speciality speciality);



}