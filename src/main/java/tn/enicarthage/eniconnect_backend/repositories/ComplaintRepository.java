package tn.enicarthage.eniconnect_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.enicarthage.eniconnect_backend.entities.Complaint;
import tn.enicarthage.eniconnect_backend.enums.ComplaintType;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    // find by student id
    List<Complaint> findByStudentId(Long studentId);


    // Add this method to ComplaintRepository.java
    @Query("SELECT COUNT(c) FROM Complaint c WHERE c.type = :type")
    int countByType(@Param("type") ComplaintType type);
}
