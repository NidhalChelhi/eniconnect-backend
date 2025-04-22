package tn.enicarthage.eniconnect_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.enicarthage.eniconnect_backend.entities.Specialization;
import tn.enicarthage.eniconnect_backend.entities.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByMatricule(String matricule);

    List<Student> findBySpecialization(Specialization specialization);

    Optional<Student> findByUserId(Long userId);

    // countbyspecializationcode
    Long countBySpecializationCode(String code);
}