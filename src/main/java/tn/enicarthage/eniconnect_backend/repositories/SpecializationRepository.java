package tn.enicarthage.eniconnect_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.enicarthage.eniconnect_backend.entities.Specialization;

import java.util.Optional;

public interface SpecializationRepository extends JpaRepository<Specialization, Long> {
    Optional<Specialization> findByCode(String code);
}