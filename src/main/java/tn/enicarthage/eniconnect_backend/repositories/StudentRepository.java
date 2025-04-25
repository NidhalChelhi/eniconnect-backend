package tn.enicarthage.eniconnect_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.enicarthage.eniconnect_backend.entities.Specialization;
import tn.enicarthage.eniconnect_backend.entities.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {


    Student findByMatricule(String matricule);

    List<Student> findBySpecialization(Specialization specialization);

    Optional<Student> findByUserId(Long userId);


    @Query("SELECT COUNT(s) FROM Student s WHERE s.specialization = :specialization")
    int countBySpecialization(@Param("specialization") Specialization specialization);

    @Query("SELECT COUNT(s) FROM Student s WHERE s.specialization.code = :code")
    long countBySpecializationCode(@Param("code") String code);


    @Query("SELECT COUNT(s) FROM Student s " +
            "WHERE s.specialization.id = :specializationId " +
            "AND s.entryYear = :entryYear")
    int countBySpecializationAndEntryYear(
            @Param("specializationId") Long specializationId,
            @Param("entryYear") Integer entryYear);
}